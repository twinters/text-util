package be.thomaswinters.text.extractors;

import be.thomaswinters.sentence.SentenceUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QuoteExtractor {
    private static final String quoteRegex = "([\"'])(\\\\?.)*?\\1";
    private static final Pattern quotePattern = Pattern.compile(quoteRegex);
    private final int minNumberWordsInQuote;

    public QuoteExtractor(int minNumberWordsInQuote) {
        this.minNumberWordsInQuote = minNumberWordsInQuote;
    }

    public QuoteExtractor() {
        this(0);
    }

    /**
     * Finds and returns all matches found by a particular Matcher object
     */
    private List<String> getAllMatches(Matcher m) {
        m.reset();
        List<String> allMatches = new ArrayList<>();
        while (m.find()) {
            String result = m.group();
            if (SentenceUtil.splitOnSpaces(result).count() >= this.minNumberWordsInQuote) {
                allMatches.add(m.group());
            }
        }
        return allMatches;
    }

    public boolean hasQuotes(String text) {
        return quotePattern.matcher(text).find();
    }

    public String removeQuotes(String text) {
        return text.replaceAll(quoteRegex, "");
    }

    public List<String> getAllMatches(String text) {
        return getAllMatches(quotePattern.matcher(text));
    }
}
