package be.thomaswinters.replacement;

import java.util.Optional;

public class SubwordDetector {

    /**
     * Returns if the selected word is a subword. Wordboundaries are from
     * startIdx (inclusive) to endIdx (exclusive)
     *
     * @param biggerText The whole text
     * @param startIdx   The starting index of the word in the whole text (inclusive)
     * @param endIdx     The ending index of the word in the whole text (exclusive)
     * @return True if the word is part of a bigger word
     */
    public static boolean isSubword(String biggerText, int startIdx, int endIdx) {
        if (startIdx > endIdx) {
            throw new IllegalArgumentException("End index is bigger than small index");
        }
        Optional<Character> beforeCharacter = getCharacterAt(biggerText, startIdx - 1);
        Optional<Character> afterCharacter = getCharacterAt(biggerText, endIdx);

        if (beforeCharacter.isPresent() && Character.isAlphabetic(beforeCharacter.get())) {
            return true;
        }
        if (afterCharacter.isPresent() && Character.isAlphabetic(afterCharacter.get())) {
            return true;
        }
        return false;
    }

    /**
     * Checks if the letter at index idx in the biggerText is part of a hashtag
     *
     * @param biggerText
     * @param idx
     * @return
     */
    public static boolean isInHashtag(String biggerText, int idx) {
        int beforeIndex = idx;
        Optional<Character> beforeCharacter = getCharacterAt(biggerText, beforeIndex);

        while (beforeCharacter.isPresent()) {
            if (beforeCharacter.get().equals(' ')) {
                return false;
            } else if (beforeCharacter.get().equals('#')) {
                return true;
            }
            beforeCharacter = getCharacterAt(biggerText, beforeIndex);
            beforeIndex = beforeIndex - 1;

        }
        return false;
    }

    public static Optional<Character> getCharacterAt(String text, int idx) {
        if (idx < 0 || idx >= text.length()) {
            return Optional.empty();
        }

        return Optional.of(text.charAt(idx));

    }

}
