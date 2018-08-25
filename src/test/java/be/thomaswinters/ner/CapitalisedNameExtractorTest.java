package be.thomaswinters.ner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CapitalisedNameExtractorTest {

    private CapitalisedNameExtractor capitalisedNameExtractor;

    @BeforeEach
    public void setup() {
        capitalisedNameExtractor = new CapitalisedNameExtractor();
    }

    @Test
    public void test_name_extraction() {
        assertEquals(Arrays.asList("Thomas Winters", "Testy McTestface"),
                capitalisedNameExtractor.findNames("Hallo ik ben Thomas Winters en dit is Testy McTestface."));
    }
}