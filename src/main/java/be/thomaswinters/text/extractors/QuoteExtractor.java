package be.thomaswinters.text.extractors;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QuoteExtractor {
    private final String quoteRegex;
    private final Pattern quotePattern;

    public QuoteExtractor(int minNumberWordsInQuote) {
        quoteRegex = createQuoteRegex(minNumberWordsInQuote);
        quotePattern = Pattern.compile(quoteRegex);
    }

    /**
     * Creates a regex that recognises a certain amount of words
     */
    private static String createQuoteRegex(int amountOfWords) {
        return "\\\"(.+\\s+){" + (amountOfWords - 1) + ",}.+\\\"";
    }

    /**
     * Finds and returns all matches found by a particular Matcher object
     */
    private static List<String> getAllMatches(Matcher m) {
        m.reset();
        List<String> allMatches = new ArrayList<>();
        while (m.find()) {
            allMatches.add(m.group());
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
