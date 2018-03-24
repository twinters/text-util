package be.thomaswinters.textgenerator.fixers;

public class TrimFixer implements ISentenceFixer {

    @Override
    public String fix(String text) {
        return text.trim();
    }

}
