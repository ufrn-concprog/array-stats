package model;

/**
 * Holds the final statistics of a run.
 */
public class RunResult {
    private final String label;
    private final double min;
    private final double max;
    private final double mean;
    private final double stddev;
    private final double time;

    /**
     * Constructor
     *
     * @param label   label describing the execution mode (e.g., "single" or "fixed(8)")
     * @param min     global minimum
     * @param max     global maximum
     * @param mean    global mean
     * @param stddev  global standard deviation (population)
     * @param time elapsed time for the run in milliseconds
     */
    public RunResult(String label, double min, double max, double mean, double stddev, double time) {
        this.label = label;
        this.min = min;
        this.max = max;
        this.mean = mean;
        this.stddev = stddev;
        this.time = time;
    }

    public double getTime() {
        return time;
    }

    @Override
    public String toString() {
        return String.format("%-8s | %10.2f | %10.2f | %10.2f | %10.2f | %.2f",
                label, min, max, mean, stddev, time);
    }
}
