package be.thomaswinters.wordcounter;

import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.Multiset.Entry;
import com.google.common.collect.Multisets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Stream;

public class WordCounter {

    public static class Builder {
        private final ImmutableMultiset.Builder b = ImmutableMultiset.builder();
        private final WordConverter converter;

        public Builder(WordConverter converter, Collection<? extends String> lines) {
            this.converter = converter;
            add(lines);
        }

        public Builder(WordConverter converter) {
            this(converter, new ArrayList<>());
        }

        public Builder() {
            this(new WordConverter());
        }

        public void add(Collection<? extends String> lines) {
            lines.stream().flatMap(converter::convertWords).forEach(b::add);
        }

        public void addWeighted(Collection<? extends String> lines, int weight) {
            lines.stream().flatMap(converter::convertWords)
                    .filter(word -> word.length() > 0)
                    .forEach(word -> b.addCopies(word, weight));
        }

        public void addWeighted(String message, int weight) {
            addWeighted(Collections.singleton(message), weight);
        }

        public void addWord(String word, int count) {
            b.addCopies(word, count);
        }

        public void addAndConvertWord(String word, int count) {
            addWord(converter.convertWord(word), count);
        }

        public ImmutableMultiset<String> buildSet() {
            return b.build();
        }

        public WordCounter build() {
            return new WordCounter(Multisets.copyHighestCountFirst(b.build()), converter);
        }


    }

    private final ImmutableMultiset<String> wordCount;
    private final WordConverter converter;

    /*-********************************************-*
     *  Constructor
     *-********************************************-*/
    private WordCounter(ImmutableMultiset<String> wordCount, WordConverter converter) {
        this.wordCount = wordCount;
        this.converter = converter;
    }

    private WordCounter(ImmutableMultiset<String> wordCount) {
        this(wordCount, new WordConverter());
    }

    public WordCounter(Collection<? extends String> lines) {
        this(new Builder(new WordConverter(), lines).buildSet());
    }

    public WordCounter(String line) {
        this(Collections.singletonList(line));
    }
    /*-********************************************-*/

    /*-********************************************-*
     *  Comparing
     *-********************************************-*/

    public int getAmountOfSameWordsAs(WordCounter other) {
        int total = 0;

        for (String word : other.getWordCount().elementSet()) {
            if (wordCount.contains(word)) {
                total += Math.min(getCount(word), other.getCount(word));
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


    /*-********************************************-*/

    /*-********************************************-*
     *  Getters
     *-********************************************-*/
    public ImmutableMultiset<String> getWordCount() {
        return wordCount;
    }

    public int getCount(String word) {
        return wordCount.count(converter.convertWord(word));
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
        return containsAny(converter.convertWords(line));
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

    public static Builder builder() {
        return new Builder();
    }

    public static WordCounter filterMininum(WordCounter wc, int minAmount) {
        Builder b = new Builder();
        wc.getWordCount().entrySet()
                .stream()
                .filter(e -> e.getCount() >= minAmount)
                .forEach(e -> b.addWord(e.getElement(), e.getCount()));
        return b.build();
    }

    public static WordCounter filterWords(WordCounter wc, Collection<String> words) {
        Builder b = new Builder();
        wc.getWordCount().entrySet()
                .stream()
                .filter(words::contains)
                .forEach(e -> b.addWord(e.getElement(), e.getCount()));
        return b.build();
    }

}
