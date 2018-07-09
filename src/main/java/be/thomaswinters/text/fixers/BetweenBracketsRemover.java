package be.thomaswinters.text.fixers;

public class BetweenBracketsRemover implements ISentenceFixer {
    @Override
    public String fix(String text) {
        return text
                .replaceAll("\\{.*?\\)", "")
                .replaceAll("\\(.*?\\)", "")
                .replaceAll("\\[.*?\\]", "");
    }
}
