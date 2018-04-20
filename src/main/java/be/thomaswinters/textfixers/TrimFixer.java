package be.thomaswinters.textfixers;

public class TrimFixer implements ISentenceFixer {

    @Override
    public String fix(String text) {
        return text.trim();
    }

}
