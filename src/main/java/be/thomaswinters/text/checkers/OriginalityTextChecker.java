package be.thomaswinters.text.checkers;

import be.thomaswinters.sentence.SentenceUtil;
import com.google.common.collect.ImmutableSet;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Checks if the generated text is original.
 *
 * @author Thomas Winters
 */
public class OriginalityTextChecker implements Predicate<String> {

    private final Collection<String> originalLines;
    private final Function<String, String> originalityNormaliser;

    public OriginalityTextChecker(Stream<String> lines, Function<String, String> originalityNormaliser) {
        this.originalityNormaliser = originalityNormaliser;
        this.originalLines = processOriginalLines(lines);

    }

    public OriginalityTextChecker(List<String> lines, Function<String, String> originalityNormaliser) {
            this(lines.stream(), originalityNormaliser);
    }

    public OriginalityTextChecker(Stream<String> originalLines) {
        this(originalLines, Function.identity());
    }

    public OriginalityTextChecker(Collection<String> originalLines) {
        this(originalLines.stream());
    }



    /*-********************************************-*
     *  Is Original Checker
     *-********************************************-*/

    /*-********************************************-*
     *  Processors
     *-********************************************-*/
    private ImmutableSet<String> processOriginalLines(Stream<? extends String> originalLines) {
        return ImmutableSet.copyOf(
                originalLines
                        // Custom normalisation
                        .map(this.originalityNormaliser)
                        // Map for originality check
                        .map(this::processLineForOriginality)
                        // Filter all empty
                        .filter(e -> !e.equals(""))
                        .iterator());
    }

    /*-********************************************-*/

    private String processLineForOriginality(String line) {
        return SentenceUtil.removePunctuations(line.replace("\n", " "))
                .replaceAll("\\s+", " ")
                .toLowerCase()
                .trim();
    }

    @Override
    public boolean test(String text) {
        String originalityText = processLineForOriginality(originalityNormaliser.apply(text));
        return !originalLines.contains(text)
                && originalLines
                .stream()
                .noneMatch(e -> e.contains(originalityText));
    }

    private Collection<String> getOriginalLines() {
        return originalLines;
    }

    /*-********************************************-*/
}
