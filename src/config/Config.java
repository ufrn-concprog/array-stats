package config;

/**
 * Central configuration for the array statistics assignment.
 * <p>
 * This class avoids command-line parsing to keep the surface area minimal.
 * Adjust the constants below to scale the workload on different machines.
 */
public final class Config {
    private Config() {}

    /** Number of logical cores used for the fixed pool size and number of tasks. */
    public static final int CORES = Runtime.getRuntime().availableProcessors();

    /** Total number of elements in the input array. */
    public static final int N = 20_000_000;

    /** Random seed for deterministic data generation. */
    public static final long SEED = 42L;

    /** Max seconds to wait for executor termination. */
    public static final int AWAIT_SECONDS = 10;
}
