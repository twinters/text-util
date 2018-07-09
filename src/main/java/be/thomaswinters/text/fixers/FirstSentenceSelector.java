package be.thomaswinters.text.fixers;

import be.thomaswinters.sentence.SentenceUtil;

public class FirstSentenceSelector implements ISentenceFixer {
    @Override
    public String fix(String text) {
        return SentenceUtil.splitInSentences(text).iterator().next();
    }
}
