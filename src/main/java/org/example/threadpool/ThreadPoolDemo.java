package org.example.threadpool;

import org.example.figure.Figure;
import org.example.supplier.FigureSupplier;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class ThreadPoolDemo {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== CUSTOM THREAD POOL DEMONSTRATION ===\n");

        ThreadPool pool = new ThreadPool(
                4,
                8,
                100,
                ThreadPool.RejectionPolicy.CALLER_RUNS
        );

        FigureSupplier supplier = new FigureSupplier();

        demonstrateBasicExecution(pool, supplier);
        demonstrateCallableTasks(pool, supplier);
        demonstrateParallelProcessing(pool, supplier);

        System.out.println("\n" + pool.getStats().getFormattedStats());

        pool.shutdown();
        boolean terminated = pool.awaitTermination(5, TimeUnit.SECONDS);
        System.out.println("\nClean shutdown: " + terminated);
        System.out.println("=== DEMONSTRATION COMPLETED ===");
    }

    private static void demonstrateBasicExecution(ThreadPool pool,
                                                  FigureSupplier supplier)
            throws InterruptedException {
        System.out.println("1) Basic task execution:");

        int taskCount = 10;
        CountDownLatch latch = new CountDownLatch(taskCount);

        for (int i = 0; i < taskCount; i++) {
            final int taskId = i;
            pool.execute(() -> {
                try {
                    Figure fig = supplier.getRandomFigure();
                    System.out.printf("[%s] Task #%d: %s (area=%.2f)%n",
                            Thread.currentThread().getName(),
                            taskId,
                            fig.getClass().getSimpleName(),
                            fig.getArea());
                } finally {
                    latch.countDown();
                }
            });
        }

        boolean done = latch.await(10, TimeUnit.SECONDS);
        if (!done) {
            System.err.println("Warning: not all basic tasks completed within timeout");
        }
    }

    private static void demonstrateCallableTasks(ThreadPool pool,
                                                 FigureSupplier supplier) {
        System.out.println("\n2) Callable tasks with Future:");

        List<Future<Double>> futures = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            Future<Double> future = pool.submit(() -> {
                Figure fig = supplier.getRandomFigure();
                sleep(ThreadLocalRandom.current().nextInt(300, 700));
                return fig.getArea();
            });
            futures.add(future);
        }

        double totalArea = futures.stream()
                .mapToDouble(f -> {
                    try {
                        return f.get(2, TimeUnit.SECONDS);
                    } catch (TimeoutException e) {
                        System.err.println("Task timed out, skipping");
                        f.cancel(true);
                        return 0.0;
                    } catch (CancellationException e) {
                        System.err.println("Task was cancelled");
                        return 0.0;
                    } catch (ExecutionException e) {
                        System.err.println("Task failed: " + e.getCause().getMessage());
                        return 0.0;
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return 0.0;
                    }
                })
                .sum();

        System.out.printf("Total area from callable tasks: %.2f%n", totalArea);
    }

    private static void demonstrateParallelProcessing(ThreadPool pool,
                                                      FigureSupplier supplier) {
        System.out.println("\n3) Parallel processing with CompletableFuture:");

        List<Figure> figures = IntStream.range(0, 20)
                .mapToObj(i -> supplier.getRandomFigure())
                .collect(Collectors.toList());

        CompletableFuture<?>[] tasks = figures.stream()
                .map(fig -> CompletableFuture
                        .runAsync(() -> processFigure(fig), pool)
                        .exceptionally(ex -> {
                            System.err.println("Figure processing failed: " + ex.getMessage());
                            return null;
                        }))
                .toArray(CompletableFuture[]::new);

        CompletableFuture.allOf(tasks).join();
        System.out.println("All figures processed!");
    }

    private static void processFigure(Figure fig) {
        System.out.printf("[%s] %s[%s] area=%.2f%n",
                Thread.currentThread().getName(),
                fig.getClass().getSimpleName(),
                fig.getColor(),
                fig.getArea());
        sleep(ThreadLocalRandom.current().nextInt(100, 400));
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
