import config.Config;
import core.StatsRunner;
import model.RunResult;

import java.util.Random;
import java.util.concurrent.Executors;

/**
 * Entry point for the program.
 *
 * <p>This program generates a large array of pseudo-random {@code double} values,
 * then computes summary statistics (min, max, mean, standard deviation) for:
 * <ol>
 *   <li>single-threaded baseline (no executor),</li>
 *   <li>fixed-size thread pool, and</li>
 *   <li>cached thread pool.</li>
 * </ol>
 * Each concurrent run creates exactly {@link Config#CORES} tasks (one per logical core), submits them with
 * {@link java.util.concurrent.ExecutorService#invokeAll(java.util.Collection)} and aggregates
 * the returned futures.</p>
 */
public final class Main {

    /**
     * Generates the input array, runs the statistics pipeline with single-threaded and
     * two pool types, and prints results.
     *
     * @param args ignored
     * @throws Exception if interrupted while awaiting pool termination
     */
    public static void main(String[] args) throws Exception {
        System.out.printf("=== Concurrent Array Statistics ===%n"
                + "n = %,d  cores = %d  tasks = %d%n%n", Config.N, Config.CORES, Config.CORES);

        double[] data = generateData(Config.N);

        StatsRunner runner = new StatsRunner(data);

        RunResult single = runner.runSingleThreaded("single");
        RunResult fixed  = runner.runWithPool(() -> Executors.newFixedThreadPool(Config.CORES),
                                        "fixed(" + Config.CORES + ")");
        RunResult cached = runner.runWithPool(java.util.concurrent.Executors::newCachedThreadPool, "cached");

        System.out.println("Pool     | min        | max        | mean       | stddev     | time (ms)");
        System.out.println("---------|------------|------------|------------|------------|---------");
        System.out.println(single);
        System.out.println(fixed);
        System.out.println(cached);

        System.out.printf("%nSpeedup vs single: fixed = %.2fx, cached = %.2fx%n",
                single.getTime() / fixed.getTime(), single.getTime() / cached.getTime());
    }

    /**
     * Creates an array of {@code n} doubles in the range [0, {@code n}).
     *
     * @param n number of elements
     * @return array of random values
     */
    private static double[] generateData(int n) {
        Random rnd = new Random();
        double[] a = new double[n];
        for (int i = 0; i < n; i++) {
            a[i] = rnd.nextDouble(1_000_000.0);
        }
        return a;
    }
}
