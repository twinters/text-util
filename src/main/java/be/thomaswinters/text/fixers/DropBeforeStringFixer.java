package be.thomaswinters.text.fixers;

import java.util.Comparator;
import java.util.Optional;
import java.util.Set;

/**
 * Drops everything from the string before the given substring
 */
public class DropBeforeStringFixer implements ISentenceFixer {
    private final Set<String> dropUntilThisString;

    public DropBeforeStringFixer(Set<String> dropUntilThisString) {
        this.dropUntilThisString = dropUntilThisString;
    }

    public DropBeforeStringFixer(String... dropUntilThisString) {
        this(Set.of(dropUntilThisString));
    }


    @Override
    public String fix(String text) {
        Optional<String> firstString = dropUntilThisString
                .stream()
                .filter(text::contains)
                .min(Comparator.comparingInt(text::indexOf));
        if (firstString.isPresent()) {
            int subStringIdx = text.indexOf(firstString.get());
            if (subStringIdx > 0) {
                return text.substring(subStringIdx + firstString.get().length());
            }
        }
        return text;
    }
}
