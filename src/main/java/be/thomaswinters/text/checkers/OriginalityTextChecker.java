package be.thomaswinters.text.checkers;

import be.thomaswinters.sentence.SentenceUtil;
import com.google.common.collect.ImmutableSet;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Checks if the generated text is original.
 *
 * @author Thomas Winters
 */
public class OriginalityTextChecker implements Predicate<String> {

    private final Collection<String> originalLines;

    public OriginalityTextChecker(Stream<String> originalLines) {
        this.originalLines = processOriginalLines(originalLines);
    }

    public OriginalityTextChecker(Collection<String> originalLines) {
        this(originalLines.stream());
    }


    /*-********************************************-*
     *  Is Original Checker
     *-********************************************-*/

    @Override
    public boolean test(String text) {
        String originalityText = processLineForOriginality(text);
        return !originalLines.contains(text)
                && originalLines
                .stream()
                .noneMatch(e -> e.contains(originalityText));
    }

    /*-********************************************-*/

    private Collection<String> getOriginalLines() {
        return originalLines;
    }

    /*-********************************************-*
     *  Processors
     *-********************************************-*/
    private static ImmutableSet<String> processOriginalLines(Stream<? extends String> originalLines) {
        return ImmutableSet.copyOf(
                originalLines
                        // Map for originality check
                        .map(OriginalityTextChecker::processLineForOriginality)
                        // Filter all empty
                        .filter(e -> !e.equals(""))
                        .iterator());
    }


    private static String processLineForOriginality(String line) {
        return SentenceUtil.removePunctuations(line.replace("\n", " ")).replaceAll("\\s+", " ").toLowerCase().trim();
    }

    /*-********************************************-*/
}
