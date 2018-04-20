package be.thomaswinters.text.fixers;

import java.util.function.UnaryOperator;

public interface ISentenceFixer extends UnaryOperator<String> {
    String fix(String text);

    @Override
    default String apply(String text) {
        return fix(text);
    }
}
