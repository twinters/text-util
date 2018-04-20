package be.thomaswinters.text.checkers;

import be.thomaswinters.sentence.SentenceUtil;
import com.google.common.collect.ImmutableList;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Checks if the generated text is original.
 *
 * @author Thomas Winters
 */
public class OriginalityTextChecker implements Predicate<String> {

    private final Collection<String> originalLines;

    public OriginalityTextChecker(Collection<String> originalLines) {
        this.originalLines = processOriginalLines(originalLines);
    }


    /*-********************************************-*
     *  Is Original Checker
     *-********************************************-*/

    @Override
    public boolean test(String text) {
        String originalityText = processLineForOriginality(text);
        return getOriginalLines().stream().noneMatch(e -> e.contains(originalityText));
    }

    /*-********************************************-*/

    private Collection<String> getOriginalLines() {
        return originalLines;
    }

    /*-********************************************-*
     *  Processors
     *-********************************************-*/
    private static ImmutableList<String> processOriginalLines(Collection<? extends String> originalLines) {
        return ImmutableList.copyOf(originalLines.stream()
                // Map for originality check
                .map(OriginalityTextChecker::processLineForOriginality)
                // Filter all empty
                .filter(e -> !e.equals("")).iterator());
    }


    private static String processLineForOriginality(String line) {
        return SentenceUtil.removePunctuations(line.replace("\n", " ")).replaceAll("\\s+", " ").toLowerCase().trim();
    }

    /*-********************************************-*/
}
