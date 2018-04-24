package be.thomaswinters.wordcounter;

import java.util.stream.Stream;

public class WordConverter {
    private final boolean ignoreCase;
    private final boolean ignoreNonAlphabetics;

    public WordConverter(boolean ignoreCase, boolean ignoreNonAlphabetics) {
        this.ignoreCase = ignoreCase;
        this.ignoreNonAlphabetics = ignoreNonAlphabetics;
    }

    public WordConverter() {
        this(true, true);
    }


    public Stream<String> convertWords(String line) {
        return Stream.of(line.split(" ")).filter(e -> e.trim().length() > 0).map(this::convertWord);
    }

    public String convertWord(String word) {
        assert !word.contains(" ");
        word = ignoreCase ? word.toLowerCase() : word;
        word = ignoreNonAlphabetics ? word.replaceAll("[^a-zA-Z]", "") : word;
        return word;
    }
}
