package be.thomaswinters.wordcounter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WordCounterTest {

    @Test
    public void simplesentence_count() {
        WordCounter wc = new WordCounter("Hello there I'm Thomas, I'm doing great!");
        assertEquals(1, wc.getCount("Hello"));
        assertEquals(1, wc.getCount("there"));
        assertEquals(2, wc.getCount("I'm"));
        assertEquals(1, wc.getCount("Thomas,"));
        assertEquals(1, wc.getCount("Thomas"));
        assertEquals(1, wc.getCount("thomas"));
        assertEquals(1, wc.getCount("doing"));
        assertEquals(1, wc.getCount("great!"));
        assertEquals(1, wc.getCount("great"));
        assertEquals(1, wc.getCount("GREAT"));
        assertEquals(0, wc.getCount("awful"));
    }

    @Test
    public void simplecomparation() {
        WordCounter wc1 = new WordCounter("I think apples are the best fruit");
        WordCounter wc2 = new WordCounter("He thinks pears are better than apples");
        WordCounter wc3 = new WordCounter("Bananas are obviously better than the best apples and the best pears are and ever will be");
//		WordCounter corpus = new WordCounter("I think apples are the best fruit He thinks pears are better than apples Bananas are obviously better than the best apples and the best pears are and ever will be");

        assertEquals(2, wc1.getAmountOfSameWordsAs(wc2));
        assertEquals(4, wc1.getAmountOfSameWordsAs(wc3));
        assertEquals(5, wc2.getAmountOfSameWordsAs(wc3));
//		System.out.println(wc1.getRelativeAmountOfSameWordsAs(wc2, corpus));
    }
}
