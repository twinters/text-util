package be.thomaswinters.sentence;


import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    void test_split_into_sentence_1() {
        assertEquals(
                Arrays.asList(
                        "In de stadsbibliotheek in Gent heeft de eerste \"Pano Live\" op locatie plaatsgevonden.",
                        "De aanwezigen debatteerden met de makers van de uitzending \"Arm Vlaanderen, vijf jaar later\", die gisteren werd uitgezonden.",
                        "In de toekomst wil \"Pano\" vaker dieper ingaan op maatschappelijke thema's op locatie met de kijkers."
                ),
                SentenceUtil.splitIntoSentences("In de stadsbibliotheek in Gent heeft de eerste \"Pano Live\" op locatie plaatsgevonden. De aanwezigen debatteerden met de makers van de uitzending \"Arm Vlaanderen, vijf jaar later\", die gisteren werd uitgezonden. In de toekomst wil \"Pano\" vaker dieper ingaan op maatschappelijke thema's op locatie met de kijkers.\n")
        );
    }

    @Test
    void test_split_into_sentence_2() {
        assertEquals(
                Arrays.asList(
                        "De Duitstalige Gemeenschap telt negen gemeenten in het uiterste oosten van het land en is goed voor een kleine 80.000 inwoners.",
                        "De regio werd in 1970 gecreëerd als Duitse Cultuurgemeenschap en had oorspronkelijk maar heel beperkte bevoegdheden.",
                        "Voor alle belangrijke beslissingen waren de Duitstaligen afhankelijk van het Waals Gewest."

                ),
                SentenceUtil.splitIntoSentences("De Duitstalige Gemeenschap telt negen gemeenten in het uiterste oosten van het land en is goed voor een kleine 80.000 inwoners. De regio werd in 1970 gecreëerd als Duitse Cultuurgemeenschap en had oorspronkelijk maar heel beperkte bevoegdheden. Voor alle belangrijke beslissingen waren de Duitstaligen afhankelijk van het Waals Gewest."
                )
        );
    }

    @Test
    void test_get_sentence_end() {
        OptionalInt sentenceEnd =
                SentenceUtil.getFirstSentenceEndIndex("De Duitstalige Gemeenschap telt negen gemeenten in het uiterste oosten van het land en is goed voor een kleine 80.000 inwoners. De regio werd in 1970 gecreëerd als Duitse Cultuurgemeenschap en had oorspronkelijk maar heel beperkte bevoegdheden. Voor alle belangrijke beslissingen waren de Duitstaligen afhankelijk van het Waals Gewest.");
        System.out.println(sentenceEnd);
        assertTrue(sentenceEnd.isPresent());
        assertEquals(126, sentenceEnd.getAsInt());

    }

    @Test
    void test_get_words() {
        List<String> words = SentenceUtil.getWords("Uiteindelijk duwt Michel het mes richting de grootste partij: \"Bart, qu'est-ce que tu en penses?\"");
        assertEquals(
                Arrays.asList("Bart", "questce", "que", "tu", "en", "penses"),
                words.subList(9, words.size()));
    }
}