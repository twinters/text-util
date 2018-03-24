package be.thomaswinters.sentence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.OptionalInt;
import java.util.PrimitiveIterator.OfInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SentenceUtil {
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

    // private static OptionalInt fixSentenceEnd(OptionalInt end, String text) {
    // if (!end.isPresent()) {
    // return end;
    // }
    // return fixSentenceEnd(end.getAsInt());
    // }

    private static IntStream getSentenceEnds(String text) {
        return Stream.of(text.indexOf('.'), text.indexOf('!'), text.indexOf('?')).filter(e -> e >= 0)
                .map(e -> fixSentenceEnd(e, text)).mapToInt(e -> e);
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

    public static List<String> splitIntoSentences(String text) {
        ArrayList<String> result = new ArrayList<>();
        OptionalInt firstEnd;
        while ((firstEnd = getFirstSentenceEndIndex(text)).isPresent()) {
            result.add(text.substring(0, firstEnd.getAsInt() + 1).trim());
            text = text.substring(firstEnd.getAsInt() + 1);
        }
        return result;
    }

    public static boolean isPunctuation(char ch) {
        return ch == '.' || ch == '!' || ch == ',' || ch == '?' || ch == ';';
    }

    public static String removePunctuations(String text) {
        return text.replaceAll("(!|,|:|;|\\.|\\?|\\(|\\))", "");
    }

    private static List<String> findNamesInSentence(String sentence) {
        List<String> foundNames = new ArrayList<>();

        List<String> words = Stream.of(sentence.split(" ")).collect(Collectors.toList());

        int start = 1;
        // Add first capitalized words only if second word is capitalized
        if (words.size() >= 2 && isCapitalized(words.get(0)) && isCapitalized(words.get(1))) {
            String name = words.get(0);
            while (start < words.size() && isCapitalized(words.get(start))) {
                name += " " + words.get(start);
                start++;
            }
            foundNames.add(name);
        }

        while (start < words.size()) {
            if (isCapitalized(words.get(start))) {
                String name = words.get(start);
                start++;
                while (start < words.size() && isCapitalized(words.get(start))) {
                    name += " " + words.get(start);
                    start++;
                }
                foundNames.add(name);
            }
            start++;
        }

        foundNames = foundNames.stream().map(e -> e.replaceAll("(!|,|:|\\.|\\?|\"|')", ""))
                .collect(Collectors.toList());
        return foundNames;
    }

    public static List<String> findNames(String text) {
        return splitInSentences(text).stream().flatMap(e -> SentenceUtil.findNamesInSentence(e).stream())
                .collect(Collectors.toList());

    }

    public static Collection<String> splitInSentences(String text) {
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
        return Stream.of(sentence.split(" ")).allMatch(word -> isCapitalized(word));
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

}
