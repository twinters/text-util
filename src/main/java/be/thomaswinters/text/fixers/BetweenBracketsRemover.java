package be.thomaswinters.text.fixers;

public class BetweenBracketsRemover implements ISentenceFixer {
    public static String removeAllBetweenBrackets(String text) {
        return text
                .replaceAll("\\{.*?\\)", "")
                .replaceAll("\\(.*?\\)", "")
                .replaceAll("\\[.*?\\]", "");

    }

    @Override
    public String fix(String text) {
        return removeAllBetweenBrackets(text);
    }
}
