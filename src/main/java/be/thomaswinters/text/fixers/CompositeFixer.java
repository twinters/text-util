package be.thomaswinters.text.fixers;

import com.google.common.collect.ImmutableList;

import java.util.Arrays;
import java.util.List;
import java.util.function.UnaryOperator;

public class CompositeFixer implements ISentenceFixer {

    private final ImmutableList<UnaryOperator<String>> fixers;

    public CompositeFixer(List<? extends UnaryOperator<String>> fixers) {
        this.fixers = ImmutableList.copyOf(fixers);
    }

    @SafeVarargs
    public CompositeFixer(UnaryOperator<String>... fixers) {
        this(Arrays.asList(fixers));
    }

    @Override
    public String fix(String text) {
        return fixers.stream().reduce(text, (e, f) -> f.apply(e), (e, f) -> e);
    }

}
