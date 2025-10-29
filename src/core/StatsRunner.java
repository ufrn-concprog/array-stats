package core;

import config.Config;
import model.Stats;
import model.RunResult;
import tasks.StatsTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Supplier;

/**
 * Coordinates the execution of statistics tasks on an array,
 * with concurrent and single-threaded variants.
 */
public final class StatsRunner {

    private final double[] data;

    /**
     * Constructs a runner for a given array.
     *
     * @param data the array to analyze
     */
    public StatsRunner(double[] data) {
        this.data = data;
    }

    /**
     * Single-threaded baseline: scans the entire array on the calling thread.
     *
     * @param label label for printing (e.g., "single")
     * @return run result with final statistics and elapsed time
     */
    public RunResult runSingleThreaded(String label) {
        long startTime = System.nanoTime();
        Stats total = new StatsTask(data, 0, data.length).call();
        return getRunResult(label, startTime, total);
    }

    /**
     * Concurrent execution using an executor supplied by {@code poolFactory}.
     *
     * @param poolFactory supplies a fresh {@link ExecutorService} (fixed or cached)
     * @param label       label used in printed results
     * @return a {@link RunResult} with final statistics and elapsed seconds
     * @throws InterruptedException if interrupted while waiting for tasks or shutdown
     */
    public RunResult runWithPool(Supplier<ExecutorService> poolFactory, String label) throws InterruptedException {
        ExecutorService pool = poolFactory.get();
        long startTime = System.nanoTime();

        // Build exactly CORES tasks (one per core)
        List<Callable<Stats>> tasks = new ArrayList<>(Config.CORES);
        int n = data.length;
        int block = Math.max(1, n / Config.CORES);
        int start = 0;
        for (int i = 0; i < Config.CORES; i++) {
            int end = (i == Config.CORES - 1) ? n : Math.min(n, start + block);
            tasks.add(new StatsTask(data, start, end));
            start = end;
        }

        Stats total = new Stats(0, 0.0, 0.0,
                Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);

        try {
            List<Future<Stats>> futures = pool.invokeAll(tasks);
            for (Future<Stats> f : futures) {
                try {
                    total = total.plus(f.get());
                } catch (ExecutionException | CancellationException e) {
                    // Treat a failed slice as contributing nothing.
                }
            }
        } finally {
            pool.shutdown();
            pool.awaitTermination(Config.AWAIT_SECONDS, TimeUnit.SECONDS);
        }

        return getRunResult(label, startTime, total);
    }

    private RunResult getRunResult(String label, long startTime, Stats total) {
        long count = total.count();
        double mean = (count == 0) ? Double.NaN : total.sum() / count;
        double variance = (count == 0) ? Double.NaN : (total.sumSq() / count) - mean * mean;
        double stddev = Math.sqrt(variance);

        double elapsedTime = (System.nanoTime() - startTime) / 1_000_000.0;
        return new RunResult(label, total.min(), total.max(), mean, stddev, elapsedTime);
    }
}
