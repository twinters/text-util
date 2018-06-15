package be.thomaswinters.sentence;

import org.junit.Test;

import static org.junit.Assert.*;

public class SentenceUtilTest {

    @Test
    public void test_first_sentence_retrieval() {
        assertEquals("er zijn hulpmiddelen beschikbaar online, die op basis van je stuk een titel voor je genereren.",
                SentenceUtil.getFirstSentence("er zijn hulpmiddelen beschikbaar online, die op basis van je stuk een titel voor je genereren. Hoe goed deze titelgeneratoren werken verschilt nogal en de kwaliteit van de titels is niet zo hoog als wanneer je er zelf een bedenkt."));
    }

}