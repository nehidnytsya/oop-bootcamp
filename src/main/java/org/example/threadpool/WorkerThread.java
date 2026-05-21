package org.example.threadpool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WorkerThread extends Thread {

    private static final Logger LOGGER = Logger.getLogger(WorkerThread.class.getName());
    private static final long POLL_TIMEOUT_MS = 100;

    private final BlockingQueue<Runnable> taskQueue;
    private final ThreadPoolStats stats;
    private final ThreadPool pool; // Посилання на батьківський пул потоків
    private final AtomicInteger tasksProcessed = new AtomicInteger(0);

    private final AtomicBoolean running = new AtomicBoolean(true);
    private final AtomicBoolean forcedStop = new AtomicBoolean(false);

    public WorkerThread(String name, BlockingQueue<Runnable> taskQueue, ThreadPoolStats stats, ThreadPool pool) {
        super(name);
        this.taskQueue = taskQueue;
        this.stats = stats;
        this.pool = pool;
        setDaemon(false);
        setUncaughtExceptionHandler((t, e) ->
                LOGGER.log(Level.SEVERE, "Uncaught exception in " + t.getName(), e)
        );
    }

    @Override
    public void run() {
        try {
            while (!forcedStop.get()) {
                if (!running.get() && taskQueue.isEmpty()) {
                    break;
                }

                try {
                    Runnable task = taskQueue.poll(POLL_TIMEOUT_MS, TimeUnit.MILLISECONDS);
                    if (task != null) {
                        executeTask(task);
                    }
                } catch (InterruptedException e) {
                    if (forcedStop.get()) {
                        break;
                    }
                }
            }
        } finally {
            pool.removeWorker(this);
        }
    }

    private void executeTask(Runnable task) {
        long startNanos = System.nanoTime();
        stats.incrementActiveTasks();
        try {
            task.run();
            stats.incrementCompletedTasks();
            tasksProcessed.incrementAndGet();
        } catch (Throwable t) {
            stats.incrementFailedTasks();
            LOGGER.log(Level.WARNING, getName() + ": task failed", t);
            if (t instanceof Error) throw (Error) t;
        } finally {
            stats.addExecutionTime(System.nanoTime() - startNanos);
            stats.decrementActiveTasks();
        }
    }

    public void shutdown() {
        running.set(false);
    }

    public void shutdownNow() {
        forcedStop.set(true);
        running.set(false);
        this.interrupt();
    }

    public int getTasksProcessed() {
        return tasksProcessed.get();
    }
}
