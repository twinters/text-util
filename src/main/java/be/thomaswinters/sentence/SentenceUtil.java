package be.thomaswinters.sentence;

import be.thomaswinters.ner.CapitalisedNameExtractor;

import java.util.*;
import java.util.PrimitiveIterator.OfInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SentenceUtil {
    private static final CapitalisedNameExtractor capitalisedNameExtractor = new CapitalisedNameExtractor();

    // private static OptionalInt fixSentenceEnd(OptionalInt end, String text) {
    // if (!end.isPresent()) {
    // return end;
    // }
    // return fixSentenceEnd(end.getAsInt());
    // }
    private static final Set<Character> punctuations = Set.of('.', '!', ',', '?', ';', ':');

    private static int fixSentenceEnd(int end, String text) {
        // Check end accolade
        int index = end;
        while (text.length() > index + 1) {
            char nextChar = text.charAt(index + 1);
            switch (nextChar) {
                case '"':
                case '\'':
                case '.':
                case '?':
                case '!':
                    index += 1;
                    break;
                default:
                    return index;
            }
        }
        return index;
    }

    private static IntStream getSentenceEnds(String text) {
        return Stream.of(text.indexOf('.'), text.indexOf('!'), text.indexOf('?'))
                .filter(e -> e >= 0)
                .map(e -> fixSentenceEnd(e, text))
                .filter(e -> e + 1 >= text.length() || Character.isSpaceChar(text.charAt(e + 1)))
                .mapToInt(e -> e);
    }

    /**
     * Gets the first end of a sentence of a text
     *
     * @param text
     * @return
     */
    public static OptionalInt getFirstSentenceEndIndex(String text) {
        return getSentenceEnds(text).min();

    }

    /**
     * Gets the last end of a sentence of a text
     *
     * @param text
     * @return
     */
    public static OptionalInt getLastSentenceEndIndex(String text) {
        return getSentenceEnds(text).max();

    }

    public static boolean isPunctuation(char ch) {
        return punctuations.contains(ch);
    }

    public static String removePunctuations(String text) {
        return text.replaceAll("([!,:;.?()])", "");
    }

    public static String trimPunctionation(String text) {
        int beginIdx = 0;
        int endIdx = text.length();
        while (beginIdx < endIdx && isPunctuation(text.charAt(beginIdx))) {
            beginIdx += 1;
        }
        while (beginIdx < endIdx && isPunctuation(text.charAt(endIdx - 1))) {
            endIdx -= 1;
        }
        return text.substring(beginIdx, endIdx);
    }

    public static boolean hasOnlyLetters(String text) {
        return text.chars().allMatch(Character::isLetter);
    }

    public static String removeNonLetters(String text) {
        return text.replaceAll("\\P{L}", "");
    }

    public static Stream<String> splitOnSpaces(String text) {
        return Stream.of(text.split("\\s"));
    }

    public static String joinWithSpaces(List<String> strings) {
        return strings.stream().collect(Collectors.joining(" "));
    }

    public static String joinWithEnters(List<String> strings) {
        return strings.stream().collect(Collectors.joining("\n"));
    }

    public static List<String> getWords(String text) {
        return getWordsStream(text).collect(Collectors.toList());
    }

    public static Stream<String> getWordsStream(String text) {
        return splitOnSpaces(text).map(SentenceUtil::removeNonLetters);
    }

    public static List<String> splitIntoSentences(String text) {
        ArrayList<String> result = new ArrayList<>();
        OptionalInt firstEnd;
        while ((firstEnd = getFirstSentenceEndIndex(text)).isPresent()) {
            result.add(text.substring(0, firstEnd.getAsInt() + 1).trim());
            text = text.substring(firstEnd.getAsInt() + 1);
        }
        return result;
    }

    public static List<String> splitInSentences(String text) {
        OfInt sentenceEnds = getSentenceEnds(text).sorted().iterator();
        List<String> result = new ArrayList<String>();

        int previousStart = 0;
        while (sentenceEnds.hasNext()) {
            int current = sentenceEnds.nextInt() + 1;

            result.add(text.substring(previousStart, current).trim());

            previousStart = current;
        }
        result.add(text.substring(previousStart, text.length()).trim());
        return result;
    }

    public static boolean isCapitalized(String word) {
        word = removePunctuations(word);
        return word.length() >= 2 && Character.isUpperCase(word.charAt(0)) && Character.isLowerCase(word.charAt(1));
    }

    public static boolean isCapitalizedSentence(String sentence) {
        return Stream.of(sentence.split(" ")).allMatch(SentenceUtil::isCapitalized);
    }

    public static boolean containsCapitalisedLetters(String input) {
        return !input.toLowerCase().equals(input);
    }

    public static String removeBetweenBrackets(String input) {
        return input.replaceAll("\\s*\\([^\\)]*\\)\\s*", " ").replaceAll(" +", " ").trim();
    }

    public static String removeBetweenSquareBrackets(String input) {
        return input.replaceAll("\\s*\\[[^\\]]*\\]\\s*", " ").replaceAll(" +", " ").trim();
    }

    public static String createString(char character, int length) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < length; i++) {
            b.append(character);
        }
        return b.toString();
    }

    /**
     * Replaces specific substring on a certain location with a replacement character
     *
     * @param text
     * @param location
     * @param toReplace
     * @param replacementCharacter
     * @return
     */
    public static String replaceCharacters(String text, int location, String toReplace, char replacementCharacter) {
        return text.substring(0, location) + createString(replacementCharacter, toReplace.length())
                + text.substring(location + toReplace.length(), text.length());
    }

    public static String getFirstSentence(String text) {
//        OptionalInt firstSentenceEnd = getFirstSentenceEndIndex(text);
//        return firstSentenceEnd.isPresent() ? text.substring(0,firstSentenceEnd.getAsInt()) : text;
        List<String> splitted = splitIntoSentences(text);
        return splitted.isEmpty() ? text : splitted.get(0);
    }

    public static String decapitalise(String s) {
        if (s.length() > 1) {
            if (s.length() > 2
                    // If the first word does not have multiple capitalised letters in the front, as this is usually
                    // An abbreviation of some sort
                    && !s.substring(0, 2).toUpperCase().equals(s.substring(0, 2))) {
                return s.substring(0, 1).toLowerCase() + s.substring(1);
            }
        }
        return s;
    }

    public static Collection<String> findNames(String text) {
        return capitalisedNameExtractor.findNames(text);
    }

    public static List<String> getParagraphs(String text) {
        return Stream.of(text.split("\n\\s*?\n")).map(String::trim).collect(Collectors.toList());
    }
}
