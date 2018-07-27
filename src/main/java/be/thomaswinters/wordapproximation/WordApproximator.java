package be.thomaswinters.wordapproximation;

import be.thomaswinters.wordapproximation.data.WordDistance;
import com.google.common.collect.ImmutableList;
import info.debatty.java.stringsimilarity.CharacterSubstitutionInterface;
import info.debatty.java.stringsimilarity.WeightedLevenshtein;

import java.util.Collection;
import java.util.stream.Stream;

public class WordApproximator implements IWordApproximator {
    private final ImmutableList<String> words;
    private final CharacterSubstitutionInterface substitutionCostCalculator;
    private final boolean allowSubstring;
    private final boolean ignoreWhitespace;

    public WordApproximator(Collection<String> words, CharacterSubstitutionInterface substitutionCostCalculator,
                            boolean allowSubstring, boolean ignoreWhitespace) {
        if (words.isEmpty()) {
            throw new IllegalArgumentException("The given list of words should contain at least one element");
        }
        this.words = ImmutableList.copyOf(words);
        this.substitutionCostCalculator = substitutionCostCalculator;
        this.allowSubstring = allowSubstring;
        this.ignoreWhitespace = ignoreWhitespace;
    }

    public WordApproximator(Collection<String> words, CharacterSubstitutionInterface substitutionCostCalculator) {
        this(words, substitutionCostCalculator, false, true);
    }

    private String process(String word) {
        if (ignoreWhitespace) {
            return word.replaceAll("\\s+", "");
        }
        return word;
    }

    private boolean containsEachother(String s1, String s2) {
        return s1.contains(s2) || s2.contains(s1);
    }

    @Override
    public Stream<WordDistance> calculateWordDistances(String inputWord) {
        WeightedLevenshtein wl = new WeightedLevenshtein(substitutionCostCalculator);
        Stream<String> wordStream = words.stream();

        // Filter out too similar words
        if (!allowSubstring) {
            wordStream = wordStream.filter(e -> !containsEachother(process(e), process(inputWord)));
        }

        return wordStream.map(word -> new WordDistance(word, wl.distance(process(inputWord), process(word))));
    }
}
