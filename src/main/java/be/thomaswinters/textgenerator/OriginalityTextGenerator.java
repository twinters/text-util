package be.thomaswinters.textgenerator;

import be.thomaswinters.sentence.SentenceUtil;
import com.google.common.collect.ImmutableList;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Checks if the generated text is original.
 *
 * @author Thomas Winters
 */
public class OriginalityTextGenerator implements ITextGenerator {

    private final Collection<String> originalLines;
    private final ITextGenerator generator;

    public OriginalityTextGenerator(Collection<String> originalLines, ITextGenerator generator) {
        this.originalLines = processOriginalLines(originalLines);
        this.generator = new ValidityCheckerTextGenerator(generator, this::isOriginal);
    }

    @Override
    public String generateText() {
        return generator.generateText();
    }

    /*-********************************************-*
     *  Is Original Checker
     *-********************************************-*/

    protected boolean isOriginal(String text) {
        String originalityText = processLineForOriginality(text);
        return !getOriginalLines().stream().anyMatch(e -> e.contains(originalityText));
    }

    /*-********************************************-*/

    public Collection<String> getOriginalLines() {
        return originalLines;
    }

    /*-********************************************-*
     *  Processors
     *-********************************************-*/
    public static ImmutableList<String> processOriginalLines(Collection<? extends String> originalLines) {
        return ImmutableList.copyOf(originalLines.stream()
                // Map for originality check
                .map(OriginalityTextGenerator::processLineForOriginality)
                // Filter all empty
                .filter(e -> !e.equals(""))
                // Collect to a list
                .collect(Collectors.toList()));
    }

    public static String processLineForOriginality(String line) {
        return SentenceUtil.removePunctuations(line.replace("\n", " ")).replaceAll("\\s+", " ").toLowerCase().trim();
    }

    /*-********************************************-*/
}
