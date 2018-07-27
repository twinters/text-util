package be.thomaswinters.wordapproximation.data;

/**
 * Value class representing the distance to a particular word
 *
 * @author Thomas Winters
 */
public class WordDistance {
    private final String word;
    private final double distance;

    public WordDistance(String word, double distance) {
        this.word = word;
        this.distance = distance;
    }

    public String getWord() {
        return word;
    }

    public double getDistance() {
        return distance;
    }

}
