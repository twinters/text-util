package be.thomaswinters.wordapproximation.fixer;

import be.thomaswinters.wordapproximation.IWordApproximator;
import be.thomaswinters.wordapproximation.data.WordDistance;

import java.util.stream.Stream;

public class WordDissimilarityIncreaser extends WordApproximatorFixer {
    public WordDissimilarityIncreaser(IWordApproximator approximator) {
        super(approximator);
    }

    @Override
    protected Stream<WordDistance> modifyStream(Stream<WordDistance> wordDistanceStream, String inputWord) {
        return wordDistanceStream.filter(e -> isDifferentEnough(e, inputWord));
    }

    private boolean isDifferentEnough(WordDistance e, String inputWord) {

        return !normalise(e.getWord()).equals(normalise(inputWord));

    }

    private String normalise(String word) {
        return word.replaceAll("[^\\w]", "");
    }
}
