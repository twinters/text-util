package be.thomaswinters.ner.data;

import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;

import java.util.List;

public class NameAnalysis {
    private final Multiset<String> names;
    private final Multiset<String> potentiallyTooLongNames;
    private final Multiset<String> beginWords;

    public NameAnalysis(Multiset<String> names, Multiset<String> potentiallyTooLongNames, Multiset<String> beginWords) {
        this.names = names;
        this.potentiallyTooLongNames = potentiallyTooLongNames;
        this.beginWords = beginWords;
    }

    public NameAnalysis(List<String> names, List<String> potentiallyTooLongNames, List<String> beginWords) {
        this(ImmutableMultiset.copyOf(names), ImmutableMultiset.copyOf(potentiallyTooLongNames), ImmutableMultiset.copyOf(beginWords));
    }

    public static NameAnalysis addAll(List<NameAnalysis> nameAnalyses) {
        return new NameAnalysis(
                nameAnalyses.stream().map(NameAnalysis::getNames).reduce(ImmutableMultiset.of(), Multisets::sum),
                nameAnalyses.stream().map(NameAnalysis::getPotentiallyTooLongNames).reduce(ImmutableMultiset.of(), Multisets::sum),
                nameAnalyses.stream().map(NameAnalysis::getBeginWords).reduce(ImmutableMultiset.of(), Multisets::sum)
        );
    }

    public Multiset<String> getNames() {
        return names;
    }

    public Multiset<String> getPotentiallyTooLongNames() {
        return potentiallyTooLongNames;
    }

    public Multiset<String> getBeginWords() {
        return beginWords;
    }
}
