package be.thomaswinters.wordapproximation;

import be.thomaswinters.wordapproximation.data.WordDistance;

import java.util.Optional;
import java.util.stream.Stream;

public interface IWordApproximator {

    default Optional<String> findBestFit(String inputWord) {
        return findBestFitWithDistance(inputWord).map(e -> e.getWord());
    }

    default Optional<WordDistance> findBestFitWithDistance(String inputWord) {
        return calculateWordDistances(inputWord)
                // Minimum distance
                .min((e, f) -> (int) Double.compare(e.getDistance(), f.getDistance()));
    }

    Stream<WordDistance> calculateWordDistances(String inputWord);

}