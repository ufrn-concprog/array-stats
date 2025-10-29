package model;

/**
 * Statistics for a slice of the input array.
 * <p>Sufficient statistics: count, sum, sum of squares, min, and max.</p>
 */
public final class Stats {
    private final long count;
    private final double sum;
    private final double sumSq;
    private final double min;
    private final double max;

    /**
     * Constructs a new immutable {@code Stats}.
     *
     * @param count number of elements
     * @param sum   sum of elements
     * @param sumSq sum of squares
     * @param min   minimum value
     * @param max   maximum value
     */
    public Stats(long count, double sum, double sumSq, double min, double max) {
        this.count = count;
        this.sum = sum;
        this.sumSq = sumSq;
        this.min = min;
        this.max = max;
    }

    /** @return number of elements observed */
    public long count() { return count; }

    /** @return sum of elements */
    public double sum() { return sum; }

    /** @return sum of squares of elements */
    public double sumSq() { return sumSq; }

    /** @return minimum element */
    public double min() { return min; }

    /** @return maximum element */
    public double max() { return max; }

    /**
     * Merges this with another partial stats instance.
     *
     * @param other other partial statistics
     * @return combined partial statistics
     */
    public Stats plus(Stats other) {
        return new Stats(
                this.count + other.count,
                this.sum + other.sum,
                this.sumSq + other.sumSq,
                Math.min(this.min, other.min),
                Math.max(this.max, other.max));
    }
}
