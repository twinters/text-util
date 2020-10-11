package be.thomaswinters.text.fixers;

import be.thomaswinters.replacement.Replacer;
import be.thomaswinters.replacement.Replacers;

import java.util.Arrays;

public class DutchDefinitionSimplifier implements ISentenceFixer {

    private final Replacers replacers = new Replacers(Arrays.asList(
            new Replacer("met betrekking tot of ", "", true, false),
            new Replacer("van of ", "", true, false),
            new Replacer("~ aan: ", "", true, false),
            new Replacer("~", "", true, false)
    ));


    public String simplyDefinition(String input) {
        String definition = replacers.replace(input)
                .trim()
                .replaceAll("\\(.*\\) ?", "")
                .replaceAll("\\{.*} ?", "")
                .replaceAll("\\[.*] ?", "")
                .replaceAll(" -.*- ", " ")
                .replaceAll("^v/m ", "")
                .replaceAll("^~?v(;|:)? ", "")
                .replaceAll("^~?m(;|:)? ", "")
                .replaceAll("^~?o(;|:)? ", "")
                .replaceAll("en ?\\/ ?of", "of")
                .replaceAll(".* [mvo] ", "")
                // Better to replace beforehand with original word:
                .replaceAll(".*~.*: ?", "")
                .replaceAll(" {2}", " ")
                .replaceAll(":", "");

        // Remove superfluous punctuation at the end.
        while (!definition.trim().isEmpty() && !Character.isAlphabetic(definition.charAt(definition.length() - 1))) {
            definition = definition.substring(0, definition.length() - 1);
        }
        return definition;
    }

    @Override
    public String fix(String text) {
        return simplyDefinition(text);
    }
}
