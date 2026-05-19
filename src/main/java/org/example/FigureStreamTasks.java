package org.example;

import org.example.figure.Circle;
import org.example.figure.Figure;
import org.example.supplier.FigureSupplier;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FigureStreamTasks {
    public static void main(String[] args) {

        FigureSupplier supplier = new FigureSupplier();
        List<Figure> figures = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            figures.add(supplier.getRandomFigure());
        }

        System.out.println("=== GENERATED FIGURES ===");
        figures.forEach(Figure::draw);

        task1FilterAndCount(figures);
        task2MapAndCollect(figures);
        task3GroupingBy(figures);
        task4Optional(figures);
    }

    // ========== 1) LAMBDA + FILTER + COUNT ==========
    private static void task1FilterAndCount(List<Figure> figures) {
        System.out.println("\n=== 1) LAMBDA + FILTER + COUNT ===");

        Predicate<Figure> isLarge = figure -> figure.getArea() > 50;

        long largeCount = figures.stream()
                .filter(isLarge)
                .count();
        System.out.printf("Figures with area > 50: %d%n", largeCount);

        Predicate<Figure> isRed = figure -> Objects.equals("red", figure.getColor());

        Predicate<Figure> isLargeAndRed = isLarge.and(isRed);

        long largeAndRedCount = figures.stream()
                .filter(isLargeAndRed)
                .count();
        System.out.printf("Figures that are large AND red: %d%n", largeAndRedCount);

        System.out.println("\n--- Large and red figures (details) ---");
        figures.stream()
                .filter(isLargeAndRed)
                .forEach(fig -> System.out.printf("  %s [%s] area=%.2f%n",
                        fig.getClass().getSimpleName(), fig.getColor(), fig.getArea()));
    }

    // ========== 2) MAP + COLLECT — LIST OF DESCRIPTIONS ==========
    private static void task2MapAndCollect(List<Figure> figures) {
        System.out.println("\n=== 2) MAP + COLLECT — LIST OF DESCRIPTIONS ===");

        List<String> descriptions = figures.stream()
                .map(fig -> String.format("%s[%s] area=%.2f",
                        fig.getClass().getSimpleName(),
                        fig.getColor(),
                        fig.getArea()))
                .collect(Collectors.toUnmodifiableList());

        System.out.println("Figure descriptions:");
        descriptions.forEach(System.out::println);
    }

    // ========== 3) GROUPINGBY — COUNT BY TYPE ==========
    private static void task3GroupingBy(List<Figure> figures) {
        System.out.println("\n=== 3) GROUPINGBY — COUNT BY TYPE ===");

        Map<String, Long> countByType = figures.stream()
                .collect(Collectors.groupingBy(
                        fig -> fig.getClass().getSimpleName(),
                        Collectors.counting()
                ));

        System.out.println("Count by figure type:");
        new TreeMap<>(countByType).forEach((type, count) ->
                System.out.printf("  %-22s: %d%n", type, count));
    }

    // ========== 4) OPTIONAL — FIRST CIRCLE ==========
    private static void task4Optional(List<Figure> figures) {
        System.out.println("\n=== 4) OPTIONAL — FIRST CIRCLE ===");

        Optional<Figure> firstCircle = figures.stream()
                .filter(fig -> fig instanceof Circle)
                .findFirst();

        System.out.println("From original list:");
        System.out.println(firstCircle
                .map(fig -> String.format("First circle found: %s[%s] area=%.2f",
                        fig.getClass().getSimpleName(),
                        fig.getColor(),
                        fig.getArea()))
                .orElse("No circle in the list"));

        List<Figure> withoutCircles = new ArrayList<>(figures);
        withoutCircles.removeIf(fig -> fig instanceof Circle);

        System.out.println("\nTesting with a list that has NO circles:");
        System.out.printf("Size after removing circles: %d (was %d)%n",
                withoutCircles.size(), figures.size());

        Optional<Figure> noCircle = withoutCircles.stream()
                .filter(fig -> fig instanceof Circle)
                .findFirst();

        System.out.println(noCircle
                .map(fig -> "Found: " + fig.getClass().getSimpleName())
                .orElse("No circle — orElse() worked safely!"));

        System.out.println("\n--- Safe Optional demonstration ---");

        System.out.println("ifPresent:");
        firstCircle.ifPresent(fig ->
                System.out.println("  Found: " + fig.getClass().getSimpleName()));

        System.out.println("orElseThrow:");
        try {
            Figure circle = firstCircle
                    .orElseThrow(() -> new RuntimeException("No circles in the list"));
            System.out.printf("  Got circle, area=%.2f%n", circle.getArea());
        } catch (RuntimeException e) {
            System.out.println("  Exception: " + e.getMessage());
        }

        System.out.println("\n.get() without check — not used a single time!");
    }
}
