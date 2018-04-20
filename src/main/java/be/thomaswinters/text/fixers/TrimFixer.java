package be.thomaswinters.text.fixers;

public class TrimFixer implements ISentenceFixer {

    @Override
    public String fix(String text) {
        return text.trim();
    }

}
