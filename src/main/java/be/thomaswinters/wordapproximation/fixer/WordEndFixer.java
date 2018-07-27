package be.thomaswinters.wordapproximation.fixer;

import be.thomaswinters.wordapproximation.IWordApproximator;
import be.thomaswinters.wordapproximation.data.WordDistance;

import java.util.stream.Stream;

/**
 * Makes sure that the ends of an approximation is the same
 */
public class WordEndFixer extends WordApproximatorFixer {
    private final int maxFixLength;

    public WordEndFixer(IWordApproximator approximator, int maxFixLength) {
        super(approximator);
        this.maxFixLength = maxFixLength;
    }

    @Override
    protected Stream<WordDistance> modifyStream(Stream<WordDistance> wordDistanceStream, String inputWord) {
        return wordDistanceStream.map(wordDistance -> fixEnd(wordDistance, inputWord));
    }

    private WordDistance fixEnd(WordDistance wordDistance, String inputWord) {
        String closestWord = wordDistance.getWord().trim();
        String fullSuffix = inputWord.substring(Math.max(0, inputWord.length() - maxFixLength));


        for (int i = 0; i < fullSuffix.length(); i++) {
            String suffix = fullSuffix.substring(fullSuffix.length() - 1 - i);
            for (int j = 0; j <= Math.min(closestWord.length() - 1, i); j++) {
                int closestWordIdx = closestWord.length() - 1 - j;
                char closeWordChar = closestWord.charAt(closestWordIdx);
                if (closeWordChar == suffix.charAt(0)) {
                    String newWord = closestWord.substring(0, closestWordIdx) + suffix;
                    return new WordDistance(newWord, wordDistance.getDistance());
                }
            }
        }
        return new WordDistance(closestWord + inputWord.substring(Math.max(0, inputWord.length() - maxFixLength)), wordDistance.getDistance());
    }
}

