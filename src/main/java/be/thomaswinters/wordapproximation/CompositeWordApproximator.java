package be.thomaswinters.wordapproximation;

import be.thomaswinters.wordapproximation.data.WordDistance;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

public class CompositeWordApproximator implements IWordApproximator {

    private final ImmutableCollection<IWordApproximator> approximators;

    public CompositeWordApproximator(Collection<IWordApproximator> approximators) {
        this.approximators = ImmutableList.copyOf(approximators);
    }

    public CompositeWordApproximator(IWordApproximator... approximators) {
        this(Arrays.asList(approximators));
    }

    @Override
    public Stream<WordDistance> calculateWordDistances(String inputWord) {
        return approximators.stream().flatMap(e -> e.calculateWordDistances(inputWord));
    }

}
