package be.thomaswinters.wordapproximation;

import be.thomaswinters.wordapproximation.data.WordDistance;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class BiWordApproximator implements IWordApproximator {
    private final IWordApproximator prefixApproximator;
    private final IWordApproximator suffixApproximator;
    private final boolean allowSameWord;

    public BiWordApproximator(IWordApproximator prefixApproximator, IWordApproximator suffixApproximator, boolean allowSameWord) {
        this.prefixApproximator = prefixApproximator;
        this.suffixApproximator = suffixApproximator;
        this.allowSameWord = allowSameWord;
    }

    @Override
    public Stream<WordDistance> calculateWordDistances(String inputWord) {

        List<WordDistance> possibilities = new ArrayList<>();

        // No empty string words
        for (int i = 1; i < inputWord.length() - 1; i++) {
            String word1 = inputWord.substring(0, i);
            String word2 = inputWord.substring(i, inputWord.length());

            Optional<WordDistance> approx1 = prefixApproximator.findBestFitWithDistance(word1);
            Optional<WordDistance> approx2 = suffixApproximator.findBestFitWithDistance(word2);

            if (approx1.isPresent() && approx2.isPresent()) {
                // Add together and replace all multiple spaces with a single space
                String totalWord = (approx1.get().getWord() + approx2.get().getWord()).replaceAll("\\s+", " ");

                // Check if they're not the same
                if (allowSameWord || !totalWord.replaceAll("\\s", "").equals(inputWord.replaceAll("\\s", ""))) {
                    possibilities.add(
                            new WordDistance(totalWord, approx1.get().getDistance() + approx2.get().getDistance()));
                }
            }

        }

        return possibilities.stream();

    }
}
