package org.example;

import org.example.figure.*;
import org.example.supplier.FigureSupplier;

import java.util.*;
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

        // ========== 1) LAMBDA + FILTER + COUNT ==========
        System.out.println("\n=== 1) LAMBDA + FILTER + COUNT ===");

        Predicate<Figure> isLarge = figure -> figure.getArea() > 50;

        long largeCount = figures.stream()
                .filter(isLarge)
                .count();
        System.out.printf("Figures with area > 50: %d%n", largeCount);

        Predicate<Figure> isRed = figure -> "red".equals(figure.getColor());

        long largeAndRedCount = figures.stream()
                .filter(isLarge.and(isRed))
                .count();
        System.out.printf("Figures that are large AND red: %d%n", largeAndRedCount);

        System.out.println("\n--- Large and red figures (details) ---");
        figures.stream()
                .filter(isLarge.and(isRed))
                .forEach(fig -> System.out.printf("  %s [%s] area=%.2f%n",
                        fig.getClass().getSimpleName(), fig.getColor(), fig.getArea()));

        // ========== 2) MAP + COLLECT — LIST OF DESCRIPTIONS ==========
        System.out.println("\n=== 2) MAP + COLLECT — LIST OF DESCRIPTIONS ===");

        List<String> descriptions = figures.stream()
                .map(fig -> String.format("%s[%s] area=%.2f",
                        fig.getClass().getSimpleName(),
                        fig.getColor(),
                        fig.getArea()))
                .collect(Collectors.toList());

        System.out.println("Figure descriptions:");
        descriptions.forEach(System.out::println);

        // ========== 3) GROUPINGBY — COUNT BY TYPE ==========
        System.out.println("\n=== 3) GROUPINGBY — COUNT BY TYPE ===");

        Map<String, Long> countByType = figures.stream()
                .collect(Collectors.groupingBy(
                        fig -> fig.getClass().getSimpleName(),
                        Collectors.counting()
                ));

        System.out.println("Count by figure type:");
        countByType.forEach((type, count) ->
                System.out.printf("  %-22s: %d%n", type, count));

        // ========== 4) OPTIONAL — FIRST CIRCLE ==========
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

        System.out.println("\nTesting with a list that has NO circles:");
        List<Figure> withoutCircles = new ArrayList<>(figures);
        withoutCircles.removeIf(fig -> fig instanceof Circle);
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
                    .filter(Circle.class::isInstance)
                    .map(Circle.class::cast)
                    .orElseThrow(() -> new RuntimeException("No circles in the list"));
            System.out.printf("  Got circle, area=%.2f%n", circle.getArea());
        } catch (RuntimeException e) {
            System.out.println("  Exception: " + e.getMessage());
        }

        System.out.println("\n.get() without check — not used a single time!");
    }
}
