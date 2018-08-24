package be.thomaswinters.text.extractors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

class QuoteExtractorTest {

    private QuoteExtractor oneWordQuoteExtractor;
    private QuoteExtractor threeWordQuoteExtractor;

    @BeforeEach
    public void setup() {
        this.oneWordQuoteExtractor = new QuoteExtractor(1);
        this.threeWordQuoteExtractor = new QuoteExtractor(3);
    }

    @Test
    public void test_enough_word_in_quote() {
        assertEquals(Collections.singletonList("\"this is a test\""),
                oneWordQuoteExtractor.getAllMatches("And then all she said to me was: \"this is a test\""));
        assertEquals(Collections.singletonList("\"this is a test\""),
                threeWordQuoteExtractor.getAllMatches("And then all she said to me was: \"this is a test\""));
        assertEquals(Collections.singletonList("\"nope\""),
                oneWordQuoteExtractor.getAllMatches("And then all she said to me was: \"nope\""));
        assertEquals(Collections.emptyList(),
                threeWordQuoteExtractor.getAllMatches("And then all she said to me was: \"nope\""));
    }
    @Test
    public void test_remove_quotes() {
        assertEquals("And then all she said to me was: ",
                oneWordQuoteExtractor.removeQuotes("And then all she said to me was: \"this is a test\""));
        assertEquals("And then all she said to me was: ",
                threeWordQuoteExtractor.removeQuotes("And then all she said to me was: \"this is a test\""));
    }

    @Test
    public void test_multiple_quotes_in_one_sentence() {
        assertEquals(Arrays.asList("\"hello there, sir\"", "\"this is a test\""),
                oneWordQuoteExtractor.getAllMatches("The barkeeper said: \"hello there, sir\" and \"this is a test\""));
        assertEquals(Arrays.asList("\"hello there, sir\"", "\"this is a test\""),
                threeWordQuoteExtractor.getAllMatches("The barkeeper said: \"hello there, sir\" and \"this is a test\""));
    }

}