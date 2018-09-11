package be.thomaswinters.sentence;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SentenceUtilTest {

    @Test
    public void test_first_sentence_retrieval() {
        assertEquals("er zijn hulpmiddelen beschikbaar online, die op basis van je stuk een titel voor je genereren.",
                SentenceUtil.getFirstSentence("er zijn hulpmiddelen beschikbaar online, die op basis van je stuk een titel voor je genereren. Hoe goed deze titelgeneratoren werken verschilt nogal en de kwaliteit van de titels is niet zo hoog als wanneer je er zelf een bedenkt."));
    }

    @Test
    void square_brackets_removal_test() {
        assertEquals("", SentenceUtil.removeBetweenSquareBrackets("[test]"));
        assertEquals("[test", SentenceUtil.removeBetweenSquareBrackets("[test"));
        assertEquals("is a", SentenceUtil.removeBetweenSquareBrackets("[test] is a [bla bla]"));
        assertEquals("how do you this", SentenceUtil.removeBetweenSquareBrackets("how do you [test] this"));
        assertEquals("", SentenceUtil.removeBetweenSquareBrackets("[Test:]"));
    }
}