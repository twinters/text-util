package be.thomaswinters.wordcounter;

import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.ImmutableMultiset.Builder;
import com.google.common.collect.Multiset.Entry;
import com.google.common.collect.Multisets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Stream;

public class WordCounter {

    public static class WordCounterBuilder {
        private final Builder<String> b;

        public WordCounterBuilder(Collection<? extends String> lines) {
            b = ImmutableMultiset.builder();
            add(lines);
        }

        public WordCounterBuilder() {
            this(new ArrayList<>());
        }

        public void add(Collection<? extends String> lines) {
            lines.stream().flatMap(WordCounter::convertWords).forEach(e -> b.add(e));
        }

        public void addWord(String word, int count) {
            b.addCopies(word, count);
        }

        public void addAndConvertWord(String word, int count) {
            addWord(WordCounter.convertWord(word), count);
        }

        public ImmutableMultiset<String> buildSet() {
            return b.build();
        }

        public WordCounter build() {
            return new WordCounter(Multisets.copyHighestCountFirst(b.build()));
        }

        public static WordCounter filterMininum(WordCounter wc, int minAmount) {
            WordCounterBuilder b = new WordCounterBuilder();
            wc.getWordCount().entrySet().stream().filter(e -> e.getCount() > minAmount)
                    .forEach(e -> b.addWord(e.getElement(), e.getCount()));
            return b.build();
        }

    }

    private final ImmutableMultiset<String> wordCount;
    private static final boolean ignoreCase = true;
    private static final boolean ignoreNonAlphabetics = true;

    /*-********************************************-*
     *  Constructor
     *-********************************************-*/
    private WordCounter(ImmutableMultiset<String> wordCount) {
        this.wordCount = wordCount;
    }

    public WordCounter(Collection<? extends String> lines) {
        this(new WordCounterBuilder(lines).buildSet());
        // Builder<String> b = ImmutableMultiset.builder();
        // for (String line : lines) {
        // convertWords(line).forEach(e -> b.add(e));
        // }
        //
        // wordCount = Multisets.copyHighestCountFirst(b.build());
    }

    public WordCounter(String line) {
        this(Arrays.asList(line));
    }
    /*-********************************************-*/

    /*-********************************************-*
     *  Comparing
     *-********************************************-*/
    public static Stream<String> convertWords(String line) {
        return Stream.of(line.split(" ")).filter(e -> e.trim().length() > 0).map(WordCounter::convertWord);
    }

    public static String convertWord(String word) {
        word = ignoreCase ? word.toLowerCase() : word;
        word = ignoreNonAlphabetics ? word.replaceAll("[^a-zA-Z]", "") : word;
        return word;
    }

    public int getAmountOfSameWordsAs(WordCounter other) {
        int total = 0;

        for (String word : other.getWordCount().elementSet()) {
            if (wordCount.contains(word)) {
                total += Math.min(getCount(word), other.getCount(word));
            }
        }

        return total;
    }

    public double getRelativeAmountOfSameWordsAs(WordCounter other, WordCounter relativeTo,
                                                 Function<Double, Function<Double, Double>> countBest) {
        double total = 0;

        for (String word : other.getWordCount().elementSet()) {
            if (wordCount.contains(word)) {

                double sameOccurrences = countBest.apply((double) getCount(word)).apply((double) other.getCount(word));
                double corpusOccurences = Math.pow(relativeTo.getCount(word), 2);

                total += (sameOccurrences) / Math.max(1, corpusOccurences);
            }
        }

        return total;
    }

    /**
     * Calculates sum of products of the frequencies of words that occur in both,
     * divided by their total word counts
     *
     * @param other
     * @return
     */
    public double getRelativeSimilarWordsAs(WordCounter other) {
        int thisTotalCount = getSize();
        int otherTotalCount = other.getSize();

        double result = 0d;

        for (String word : getWordCount().elementSet()) {
            if (other.contains(word)) {

                double thisFrequency = ((double) getCount(word)) / ((double) thisTotalCount);
                double otherFrequency = ((double) other.getCount(word)) / ((double) otherTotalCount);

                result += thisFrequency * otherFrequency;

            }
        }

        return result;

    }

    public double getRelativeAmountOfSameWordsAs(WordCounter other, WordCounter relativeTo) {
        return getRelativeAmountOfSameWordsAs(other, relativeTo, e -> f -> Math.min(e, f));
    }

    public double getRelativeAmountOfSameMaxWordsAs(WordCounter other, WordCounter relativeTo) {
        return getRelativeAmountOfSameWordsAs(other, relativeTo, e -> f -> Math.max(e, f));
    }
    /*-********************************************-*/

    /*-********************************************-*
     *  Getters
     *-********************************************-*/
    public ImmutableMultiset<String> getWordCount() {
        return wordCount;
    }

    public int getCount(String word) {
        return wordCount.count(convertWord(word));
    }

    public boolean isIgnoreCase() {
        return ignoreCase;
    }

    public int getSize() {
        return wordCount.size();
    }

    public int getDistinctSize() {
        return wordCount.elementSet().size();
    }

    public int getAverageWordCount() {
        return getSize() / getDistinctSize();
    }

    public boolean contains(String word) {
        return wordCount.contains(word);
    }

    public boolean containsAny(Stream<String> words) {
        return words.anyMatch(this::contains);
    }

    public boolean containsAny(Collection<String> words) {
        return containsAny(words.stream());
    }

    public boolean containsAny(String line) {
        return containsAny(convertWords(line));
    }

    /*-********************************************-*/
    public String getQuartileWord(double quartile) {
        if (quartile < 0 || quartile >= 1) {
            throw new IllegalArgumentException("Invalid quartile: " + quartile);
        }
        int goalSize = (int) Math.floor(quartile * (double) getSize());

        int currentSize = 0;
        for (Entry<String> entry : wordCount.entrySet()) {
            if (currentSize < goalSize) {
                currentSize += entry.getCount();
            } else {
                return entry.getElement();
            }
        }
        throw new IllegalStateException("Ran over all elements");
    }

    public int getQuartileCount(double quartile) {
        return getCount(getQuartileWord(quartile));
    }

    public Collection<String> getElements() {
        return wordCount.elementSet();
    }

    @Override
    public String toString() {
        return "WordCounter [" + wordCount + "]";
    }


}
