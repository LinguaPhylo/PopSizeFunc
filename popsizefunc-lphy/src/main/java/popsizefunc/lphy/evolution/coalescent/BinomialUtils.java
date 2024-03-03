package popsizefunc.lphy.evolution.coalescent;

public class BinomialUtils {
    /**
     * Count the number of ways to select two out of n elements.
     *
     * @param n total number of elements
     * @return The number of ways to select two from n elements
     */
    public static double choose2(final int n) {
        return n * (n - 1) / 2.0;
    }
}
