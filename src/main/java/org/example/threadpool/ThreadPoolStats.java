package org.example.threadpool;


import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ThreadPoolStats {

    private final AtomicInteger activeTasks      = new AtomicInteger(0);
    private final AtomicLong totalSubmitted      = new AtomicLong(0);
    private final AtomicLong totalCompleted      = new AtomicLong(0);
    private final AtomicLong totalFailed         = new AtomicLong(0);
    private final AtomicLong totalExecutionNanos = new AtomicLong(0);

    private final Instant startTime              = Instant.now();

    public void incrementActiveTasks()       { activeTasks.incrementAndGet(); }
    public void decrementActiveTasks()       { activeTasks.decrementAndGet(); }
    public void incrementSubmitted()         { totalSubmitted.incrementAndGet(); }
    public void incrementCompletedTasks()    { totalCompleted.incrementAndGet(); }
    public void incrementFailedTasks()       { totalFailed.incrementAndGet(); }
    public void addExecutionTime(long nanos) { totalExecutionNanos.addAndGet(nanos); }

    public int  getActiveTasks() { return activeTasks.get(); }
    public long getSubmitted()   { return totalSubmitted.get(); }
    public long getCompleted()   { return totalCompleted.get(); }
    public long getFailed()      { return totalFailed.get(); }

    public double getAvgExecutionMillis() {
        long completed = totalCompleted.get();
        if (completed == 0) return 0.0;
        return totalExecutionNanos.get() / (double) completed / 1_000_000.0;
    }

    public String getFormattedStats() {
        Duration uptime = Duration.between(startTime, Instant.now());
        long minutes = uptime.toMinutes();
        long seconds = uptime.toSeconds() % 60;
        long millis = uptime.toMillis() % 1000;

        return "Pool Statistics:\n"
                + "+-----------------------+\n"
                + String.format("| Running time   : %dm %02d.%03ds%n", minutes, seconds, millis)
                + String.format("| Active tasks   : %d%n",        activeTasks.get())
                + String.format("| Submitted      : %d%n",        totalSubmitted.get())
                + String.format("| Completed      : %d%n",        totalCompleted.get())
                + String.format("| Failed         : %d%n",        totalFailed.get())
                + String.format("| Avg exec time  : %.2f ms%n",   getAvgExecutionMillis())
                + "+-----------------------+";
    }

    @Override
    public String toString() {
        return getFormattedStats();
    }
}
