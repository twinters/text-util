package be.thomaswinters.ner;

import be.thomaswinters.sentence.SentenceUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CapitalisedNameExtractor {

    public List<String> findNames(String text) {
        return SentenceUtil.splitInSentences(text)
                .stream()
                .flatMap(e -> Stream.of(e.split("\n")))
                .flatMap(e -> findNamesInSentence(e).stream())
                .collect(Collectors.toList());

    }

    private List<String> findNamesInSentence(String sentence) {
        List<String> foundNames = new ArrayList<>();

        List<String> words = Stream.of(sentence.split(" ")).collect(Collectors.toList());

        int start = 1;
        // Add first capitalized words only if second word is capitalized
        if (words.size() >= 2 && isPotentialName(words.get(0)) && isPotentialName(words.get(1))) {
            StringBuilder name = new StringBuilder(words.get(0));
            while (start < words.size() && isPotentialName(words.get(start))) {
                name.append(" ").append(words.get(start));
                start++;
            }
            foundNames.add(name.toString());
        }

        while (start < words.size()) {
            if (isPotentialName(words.get(start))) {
                StringBuilder name = new StringBuilder(words.get(start));
                start++;
                while (start < words.size() && isPotentialName(words.get(start))) {
                    name.append(" ").append(words.get(start));
                    start++;
                }
                foundNames.add(name.toString());
            }
            start++;
        }

        foundNames = foundNames
                .stream()
                .map(e -> e.replaceAll("([!,:.?\"'])", ""))
                .collect(Collectors.toList());
        return foundNames;
    }

    private boolean isPotentialName(String word) {
        return SentenceUtil.isCapitalized(word);
    }
}
