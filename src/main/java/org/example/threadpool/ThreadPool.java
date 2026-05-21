package org.example.threadpool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPool extends AbstractExecutorService {

    public enum RejectionPolicy {
        ABORT, CALLER_RUNS, DISCARD, DISCARD_OLDEST
    }

    private final BlockingQueue<Runnable> taskQueue;

    private final List<WorkerThread> workers = new ArrayList<>();

    private final AtomicBoolean isRunning = new AtomicBoolean(true);
    private final ThreadPoolStats stats;
    private final RejectionPolicy rejectionPolicy;

    private final int corePoolSize;
    private final int maxPoolSize;
    private final int maxQueueSize;

    private final AtomicInteger threadCounter = new AtomicInteger(0);

    public ThreadPool(int corePoolSize, int maxPoolSize, int queueCapacity, RejectionPolicy policy) {
        if (corePoolSize <= 0 || maxPoolSize < corePoolSize || queueCapacity <= 0) {
            throw new IllegalArgumentException("Invalid pool parameters");
        }
        this.corePoolSize = corePoolSize;
        this.maxPoolSize = maxPoolSize;
        this.maxQueueSize = queueCapacity;
        this.rejectionPolicy = policy;
        this.taskQueue = new LinkedBlockingQueue<>(queueCapacity);
        this.stats = new ThreadPoolStats();
    }

    public ThreadPool(int poolSize) {
        this(poolSize, poolSize, Integer.MAX_VALUE / 2, RejectionPolicy.ABORT);
    }

    void removeWorker(WorkerThread worker) {
        synchronized (workers) {
            workers.remove(worker);
        }
    }

    private boolean addWorker() {
        if (workers.size() >= maxPoolSize) {
            return false;
        }
        WorkerThread worker = new WorkerThread(
                "pool-worker-" + threadCounter.incrementAndGet(),
                taskQueue,
                stats,
                this
        );
        workers.add(worker);
        worker.start();
        return true;
    }

    @Override
    public void execute(Runnable command) {
        if (command == null) throw new NullPointerException("Task must not be null");

        stats.incrementSubmitted();

        if (!isRunning.get()) {
            throw new RejectedExecutionException("ThreadPool is shut down");
        }

        if (getWorkersCount() < corePoolSize) {
            synchronized (workers) {
                if (workers.size() < corePoolSize && addWorker()) {
                    taskQueue.offer(command);
                    return;
                }
            }
        }

        if (taskQueue.offer(command)) {
            return;
        }

        synchronized (workers) {
            if (workers.size() < maxPoolSize && addWorker()) {
                if (taskQueue.offer(command)) {
                    return;
                }
            }
        }
        handleRejection(command);
    }

    private void handleRejection(Runnable task) {
        switch (rejectionPolicy) {
            case ABORT:
                throw new RejectedExecutionException("Task rejected: queue is full");
            case CALLER_RUNS:
                if (isRunning.get()) {
                    task.run();
                }
                break;
            case DISCARD:
                break;
            case DISCARD_OLDEST:
                Runnable discarded = taskQueue.poll();
                if (discarded != null && !taskQueue.offer(task)) {
                    throw new RejectedExecutionException("Queue full after discard");
                }
                break;
        }
    }

    @Override
    public void shutdown() {
        if (isRunning.compareAndSet(true, false)) {
            synchronized (workers) {
                for (WorkerThread worker : workers) {
                    worker.shutdown();
                }
            }
        }
    }

    @Override
    public List<Runnable> shutdownNow() {
        List<Runnable> pending = new ArrayList<>();
        if (isRunning.compareAndSet(true, false)) {
            taskQueue.drainTo(pending);
            synchronized (workers) {
                for (WorkerThread worker : workers) {
                    worker.shutdownNow();
                }
            }
        }
        return pending;
    }

    @Override
    public boolean isShutdown() {
        return !isRunning.get();
    }

    @Override
    public boolean isTerminated() {
        if (isRunning.get()) return false;
        synchronized (workers) {
            return workers.isEmpty();
        }
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        long deadline = System.nanoTime() + unit.toNanos(timeout);

        List<WorkerThread> toJoin;
        synchronized (workers) {
            toJoin = new ArrayList<>(workers);
        }

        for (WorkerThread w : toJoin) {
            long remainingNanos = deadline - System.nanoTime();
            if (remainingNanos <= 0) break;

            long remainingMillis = TimeUnit.NANOSECONDS.toMillis(remainingNanos);
            if (remainingMillis <= 0) remainingMillis = 1;

            w.join(remainingMillis);
        }

        return isTerminated();
    }

    public int getActiveWorkers() {
        synchronized (workers) {
            return (int) workers.stream().filter(Thread::isAlive).count();
        }
    }

    private int getWorkersCount() {
        synchronized (workers) {
            return workers.size();
        }
    }

    public int getQueueSize() {
        return taskQueue.size();
    }

    public ThreadPoolStats getStats() {
        return stats;
    }
}
