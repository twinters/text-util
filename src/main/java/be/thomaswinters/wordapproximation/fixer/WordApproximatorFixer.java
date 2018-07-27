package be.thomaswinters.wordapproximation.fixer;

import be.thomaswinters.wordapproximation.IWordApproximator;
import be.thomaswinters.wordapproximation.data.WordDistance;

import java.util.stream.Stream;

public abstract class WordApproximatorFixer implements IWordApproximator {

    private final IWordApproximator approximator;

    public WordApproximatorFixer(IWordApproximator approximator) {
        this.approximator = approximator;
    }

    @Override
    public Stream<WordDistance> calculateWordDistances(String inputWord) {
        return modifyStream(approximator.calculateWordDistances(inputWord), inputWord);
    }

    protected abstract Stream<WordDistance> modifyStream(Stream<WordDistance> wordDistanceStream, String inputWord);
}
