package be.thomaswinters.text.fixers;

import be.thomaswinters.sentence.SentenceUtil;

import java.util.OptionalInt;

/**
 * This class cuts multisentence texts such that it is below a certain max length, but still has punctuation at the end.
 */
public class SentenceShortener implements ISentenceFixer {

    private final int maxLength;

    public SentenceShortener(int maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    public String fix(String sentences) {

        // Cut off sentences before applying other things
        if (sentences.length() > (maxLength - 1)) {

            // Try shortening to premise & conclusion
            OptionalInt conclusionStart = SentenceUtil.getLastSentenceEndIndex(sentences.substring(0, sentences.length() - 3));
            OptionalInt premiseEnd = SentenceUtil.getFirstSentenceEndIndex(sentences);
            if (premiseEnd.isPresent() && conclusionStart.isPresent()) {
                String premiseConclusion = sentences.substring(0, premiseEnd.getAsInt() + 1)
                        + sentences.substring(conclusionStart.getAsInt() + 1, sentences.length());
                if (premiseConclusion.length() < (maxLength - 1)) {
                    sentences = premiseConclusion.trim();
                } else {
                    // If previous trial too long: just abbreviate by finding
                    // first end before 140 characters
                    sentences = roughCut(sentences);
                }
            }

        }
        return sentences;
    }

    public String roughCut(String tweet) {
        String roughCutText = tweet.substring(0, maxLength - 1);
        OptionalInt lastIndex = SentenceUtil
                .getLastSentenceEndIndex(roughCutText.substring(0, roughCutText.length() - 1));
        if (lastIndex.isPresent()) {
            String newTweet = roughCutText.substring(0, lastIndex.getAsInt() + 1);
            return newTweet.trim();
        }
        return roughCutText;
    }

}
