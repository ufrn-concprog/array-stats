package tasks;

import model.Stats;

import java.util.concurrent.Callable;

/**
 * {@link Callable} that computes partial statistics over a slice of a {@code double[]} array.
 * <p>The slice is the half-open interval {@code [start, end)}.</p>
 */
public final class StatsTask implements Callable<Stats> {
    private final double[] data;
    private final int start;
    private final int end;

    /**
     * Creates a new task scanning {@code data[start, end)}.
     *
     * @param data  the full input array
     * @param start inclusive start index
     * @param end   exclusive end index
     */
    public StatsTask(double[] data, int start, int end) {
        this.data = data;
        this.start = start;
        this.end = end;
    }

    /** {@inheritDoc} */
    @Override
    public Stats call() {
        if (start >= end) {
            return new Stats(0, 0.0, 0.0, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
        }
        long count = 0;
        double sum = 0.0, sumSq = 0.0;
        double min = Double.POSITIVE_INFINITY, max = Double.NEGATIVE_INFINITY;

        // TODO: Implement the computation of the values for a Stats object

        return new Stats(count, sum, sumSq, min, max);
    }
}
