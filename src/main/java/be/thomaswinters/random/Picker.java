package be.thomaswinters.random;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

public class Picker {

    private static final Random RANDOM = new Random();

    public static <E> E pick(Collection<E> values) {
        return new ArrayList<E>(values).get(RANDOM.nextInt(values.size()));
    }

    /**
     * Use this function if you want some input converted into an output with a
     * certain chance, and return defaultOutput if the event failed.
     *
     * @param input         The input to process
     * @param event         The converting event to happen
     * @param defaultOutput The output to output when the event failed.
     * @param chance        The chance it will happen. Must be in the interval [0,1]
     * @return
     */
    public static <I, O> O chanceEvent(I input, Function<I, O> event, O defaultOutput, double chance) {
        if (chance < 0 || chance > 1) {
            throw new IllegalArgumentException("The chance of the event must be between 0 and 1");
        }
        if (chance > RANDOM.nextDouble()) {
            return event.apply(input);
        }
        return defaultOutput;
    }

    /**
     * Java's Random does not has a "Next Long" :c
     *
     * @param n
     * @return
     */
    public static long nextLong(long n) {
        long bits, val;
        do {
            bits = (RANDOM.nextLong() << 1) >>> 1;
            val = bits % n;
        } while (bits - val + (n - 1) < 0L);
        return val;
    }

    public static void setSeed(long number) {
        RANDOM.setSeed(number);
    }

    public static int betweenInclusive(int min, int max) {
        if (max < min) {
            throw new IllegalArgumentException("Minimum should be lower than maximum");
        }
        return min + RANDOM.nextInt(max + 1 - min);
    }

    public static <E> E pickWeighted(Map<E, Integer> map) {

        int totalSize = map.values().stream().mapToInt(e -> e).sum();

        int chosenIndex = RANDOM.nextInt(totalSize);

        int currentIndex = 0;
        for (Map.Entry<E, Integer> entry : map.entrySet()) {
            currentIndex += entry.getValue();
            if (chosenIndex < currentIndex) {
                return entry.getKey();
            }
        }
        throw new IllegalStateException("Not able to pick a random element from " + map);
    }
}
