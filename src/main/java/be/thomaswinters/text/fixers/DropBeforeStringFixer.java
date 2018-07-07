package be.thomaswinters.text.fixers;

/**
 * Drops everything from the string before the given substring
 */
public class DropBeforeStringFixer implements ISentenceFixer {
    private final String dropUntilThisString;

    public DropBeforeStringFixer(String dropUntilThisString) {
        this.dropUntilThisString = dropUntilThisString;
    }

    @Override
    public String fix(String text) {
        int substringIdx = text.indexOf(dropUntilThisString);
        if (substringIdx > 0) {
            return text.substring(substringIdx + dropUntilThisString.length());
        }
        return "";
    }
}
