package be.thomaswinters.replacement;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Replacer implements IReplacer {
    private final String word;
    private final String replacementWord;
    private final boolean replaceSubwords;
    private final Pattern pattern;
    private final boolean matchCase;

    /**
     * Creates a replacer object that is capable to replace all occurrences of a
     * string with another string
     *
     * @param word            Word to replace
     * @param replacementWord Word that will end up on the original words place
     * @param replaceSubwords Is this replacer allowed to replace this word if it is part of
     *                        a bigger word?
     */
    public Replacer(String word, String replacementWord, boolean replaceSubwords, boolean matchCase) {
        if (word.length() == 0) {
            throw new IllegalArgumentException("Invalid word to replace: Zero length");
        }

        this.word = word;
        this.replacementWord = replacementWord;
        this.replaceSubwords = replaceSubwords;
        this.matchCase = matchCase;
        this.pattern = Pattern.compile(word, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    }

    public Replacer(String word, String replacementWord, boolean replaceSubwords) {
        this(word, replacementWord, replaceSubwords, true);
    }

    public Replacer(String word, String replacementWord) {
        this(word, replacementWord, true);
    }

    public static List<Replacer> createReplacers(String word, List<String> replacementsWords, boolean replaceSubwords, boolean matchCase) {
        return replacementsWords.stream()
                .map(replacementWord -> new Replacer(word, replacementWord, replaceSubwords, matchCase))
                .collect(Collectors.toList());
    }

    /*-********************************************-*
     *  Data
     *-********************************************-*/
    public String getWord() {
        return word;
    }

    public String getReplacementWord() {
        return replacementWord;
    }
    /*-********************************************-*/

    /*-********************************************-*
     *  Actions
     *-********************************************-*/
    @Override
    public Replacement replace(Replacement replacement) {

        String text = replacement.getText();
        int amountOfReplacementsDone = 0;

        Matcher matcher = pattern.matcher(text);
        StringBuilder builder = new StringBuilder();

        int lastEnd = 0;
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();

            builder.append(text.substring(lastEnd, start));

            String wordToReplace = text.substring(start, end);
            if (replaceSubwords || !SubwordDetector.isSubword(text, start, end)) {
                boolean isInHashtag = SubwordDetector.isInHashtag(text, start);

                String replacedText;
                if (matchCase) {
                    replacedText = getReplacementWordSameCase(wordToReplace, isInHashtag);
                } else {
                    replacedText = replacementWord;
                }

                if (isInHashtag) {
                    replacedText = replacedText.replaceAll(" ", "");
                }

                builder.append(replacedText);
                amountOfReplacementsDone += 1;
            } else {
                builder.append(wordToReplace);
            }

            // Update where last end was
            lastEnd = end;
        }
        builder.append(text.substring(lastEnd));

        return replacement.createNew(builder.toString(), this, amountOfReplacementsDone);
        // return pattern.matcher(text).replaceAll(replacementWord);
    }

    @Override
    public String replace(String text) {
        return replace(new Replacement(text)).getText();
    }

    private String getReplacementWordSameCase(String wordCaseSensitive, boolean capitalizeEveryWord) {
        if (wordCaseSensitive.toLowerCase().equals(wordCaseSensitive)) {
            return replacementWord.toLowerCase();
        }
        if (wordCaseSensitive.toUpperCase().equals(wordCaseSensitive)) {
            return replacementWord.toUpperCase();
        }
        if (wordCaseSensitive.substring(0, 1).toUpperCase().equals(wordCaseSensitive.substring(0, 1))
                && wordCaseSensitive.substring(1).toLowerCase().equals(wordCaseSensitive.substring(1))) {

            if (capitalizeEveryWord) {
                List<String> parts = Arrays.asList(replacementWord.split(" "));
                return parts.stream().map(e -> e.substring(0, 1).toUpperCase() + e.substring(1).toLowerCase())
                        .collect(Collectors.joining(" "));
            }
            return replacementWord.substring(0, 1).toUpperCase() + replacementWord.substring(1).toLowerCase();
        }
        return replacementWord;
    }



    /*-********************************************-*/

    /*-********************************************-*
     *  Aggregate
     *-********************************************-*/
    // public int countReferences(String text) {
    // Matcher matcher = pattern.matcher(text);
    // int count = 0;
    // while (matcher.find()) {
    // count++;
    // }
    // return count;
    // }
    /*-********************************************-*/

    /*-********************************************-*
     *  Hashcode & Equals
     *-********************************************-*/
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((pattern == null) ? 0 : pattern.hashCode());
        result = prime * result + (replaceSubwords ? 1231 : 1237);
        result = prime * result + ((replacementWord == null) ? 0 : replacementWord.hashCode());
        result = prime * result + ((word == null) ? 0 : word.hashCode());
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Replacer other = (Replacer) obj;
        if (pattern == null) {
            if (other.pattern != null)
                return false;
        } else if (!pattern.equals(other.pattern))
            return false;
        if (replaceSubwords != other.replaceSubwords)
            return false;
        if (replacementWord == null) {
            if (other.replacementWord != null)
                return false;
        } else if (!replacementWord.equals(other.replacementWord))
            return false;
        if (word == null) {
            if (other.word != null)
                return false;
        } else if (!word.equals(other.word))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "" + word + "->" + replacementWord + (replaceSubwords ? "(Subwords)" : "");
    }

    /*-********************************************-*/
}
