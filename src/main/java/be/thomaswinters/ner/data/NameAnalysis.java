package be.thomaswinters.ner.data;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class NameAnalysis {
    private final List<String> names;
    private final List<String> potentiallyTooLongNames;
    private final List<String> beginWords;

    public NameAnalysis(List<String> names, List<String> potentiallyTooLongNames, List<String> beginWords) {
        this.names = names;
        this.potentiallyTooLongNames = potentiallyTooLongNames;
        this.beginWords = beginWords;
    }

    public static NameAnalysis addAll(List<NameAnalysis> nameAnalyses) {
        return new NameAnalysis(
                nameAnalyses.stream().map(NameAnalysis::getNames).flatMap(Collection::stream).collect(Collectors.toList()),
                nameAnalyses.stream().map(NameAnalysis::getPotentiallyTooLongNames).flatMap(Collection::stream).collect(Collectors.toList()),
                nameAnalyses.stream().map(NameAnalysis::getBeginWords).flatMap(Collection::stream).collect(Collectors.toList())
        );
    }

    public List<String> getNames() {
        return names;
    }

    public List<String> getPotentiallyTooLongNames() {
        return potentiallyTooLongNames;
    }

    public List<String> getBeginWords() {
        return beginWords;
    }
}
