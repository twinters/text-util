package be.thomaswinters.ner;

import be.thomaswinters.ner.data.NameAnalysis;
import be.thomaswinters.sentence.SentenceUtil;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CapitalisedNameExtractor {

    private static final Set<String> PROHIBITED_NAMES = Set.of("de", "het", "van", "voor", "ze", "volgens", "ik", "we","jullie", "je", "hij", "zij");

    public Multiset<String> findNames(String text) {
        return processSentencesNameAnalysises(SentenceUtil.splitIntoSentences(text)
                .stream()
                .flatMap(e -> Stream.of(e.split("\n")))
                .flatMap(e -> Stream.of(e.split("\"")))
                .map(this::findNamesInSentence)
                .collect(Collectors.toList()));

    }

    private Multiset<String> processSentencesNameAnalysises(List<NameAnalysis> sentenceAnalysises) {
        NameAnalysis nameAnalysis = NameAnalysis.addAll(sentenceAnalysises);

        // Add all "normal" names: they're safe
        Multiset<String> result = HashMultiset.create(nameAnalysis.getNames());
        System.out.println("\n\n********************************\nSTARTING FINAL AGGREGATE ANALYSIS:");
        System.out.println("CERTAIN NAMES: " + Multisets.copyHighestCountFirst(result));

        // Only add "potentially too long names" if they are already in the names set:
        System.out.println("\n\n\nEXAMINING LONG NAMES: " + Multisets.copyHighestCountFirst(nameAnalysis.getPotentiallyTooLongNames()));
        for (String name : nameAnalysis.getPotentiallyTooLongNames()) {
            if (result.contains(name)) {
                result.add(name);
            }
        }
        System.out.println("ADDED POTENTIALLY TOO LONG NAMES TO RESULT: " + Multisets.copyHighestCountFirst(result) + "\n\n\n");

        // Only add "begin of sentence" words if they're already part of something in the names set:
        System.out.println("\n\n\nEXAMINING BEGIN WORDS: " + Multisets.copyHighestCountFirst(nameAnalysis.getBeginWords()));
        Multiset<String> beginWordsToAdd = HashMultiset.create();
        for (String word : nameAnalysis.getBeginWords()) {
            if (!PROHIBITED_NAMES.contains(word.toLowerCase())) {
                // Get all names containing this word
                List<String> namesContainingWord = result
                        .stream()
                        .filter(name -> name.contains(word))
                        .collect(Collectors.toList());

                // Add all these names
                if (!namesContainingWord.isEmpty()) {
                    System.out.println("Adding" + word);
                    beginWordsToAdd.add(word);
                }
            }
        }
        System.out.println("ADDED BEGIN WORDS : " + Multisets.copyHighestCountFirst(beginWordsToAdd) + "\n\n");
        result.addAll(beginWordsToAdd);
        System.out.println("Current Result: " + Multisets.copyHighestCountFirst(result) + "\n\n");

        // Increase the count of names that are longer than other names
        result = increaseLongerNamesCount(result);

        return result;
    }

    /**
     * Increases all names that contain other names with their counts
     */
    private Multiset<String> increaseLongerNamesCount(Multiset<String> nameCounts) {

        Multiset<String> result = HashMultiset.create(nameCounts);
        for (Multiset.Entry<String> entry : nameCounts.entrySet()) {

            for (String otherName : nameCounts.elementSet()) {
                if (otherName.contains(entry.getElement())) {
                    result.add(otherName, entry.getCount());
                }
            }
        }

        return result;
    }


    private NameAnalysis findNamesInSentence(String sentence) {
        sentence = sentence.trim();
        System.out.println("DEALING WITH SENTENCE: " + sentence);
        List<String> names = new ArrayList<>();
        List<String> potentiallyTooLongNames = new ArrayList<>();
        List<String> beginWords = new ArrayList<>();

        List<String> words = SentenceUtil
                .getWordsStream(sentence)
                .filter(e -> e.trim().length() > 0)
                .collect(Collectors.toList());

        int start = 1;
        // Add first capitalized words only if second word is capitalized
        if (!words.isEmpty() && isPotentialName(words.get(0))) {
            if (words.size() >= 2 && isPotentialName(words.get(1))) {
                StringBuilder name = new StringBuilder(words.get(0));
                while (start < words.size() && isPotentialName(words.get(start))) {
                    name.append(" ").append(words.get(start));
                    start++;
                }
                System.out.println("beginname" + name.toString());
                potentiallyTooLongNames.add(name.toString());
            } else {
                beginWords.add(words.get(0));
            }
        }

        while (start < words.size()) {
            if (isPotentialName(words.get(start))) {
                StringBuilder name = new StringBuilder(words.get(start));
                start++;
                while (start < words.size() && isPotentialName(words.get(start))) {
                    name.append(" ").append(words.get(start));
                    start++;
                }
                System.out.println("sentencename: " + start + ": " + name.toString());
                names.add(name.toString());
            }
            start++;
        }

        names = names
                .stream()
                .map(e -> e.replaceAll("([!,:.?\"'])", ""))
                .collect(Collectors.toList());
        return new NameAnalysis(names, potentiallyTooLongNames, beginWords);
    }

    private boolean isPotentialName(String word) {
        return SentenceUtil.isCapitalized(word);
    }
}
