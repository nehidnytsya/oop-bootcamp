package org.example;

import org.example.figure.*;
import org.example.supplier.FigureSupplier;

import java.util.*;

public class FigureMapTasks {

    public static void main(String[] args) {

        FigureSupplier supplier = new FigureSupplier();
        List<Figure> figures = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            figures.add(supplier.getRandomFigure());
        }

        System.out.println("=== GENERATED FIGURES ===");
        figures.forEach(Figure::draw);


        System.out.println("\n=== 1) GROUPING BY TYPE ===");

        Map<String, List<Figure>> byType = new HashMap<>();

        for (Figure fig : figures) {
            String typeName = fig.getClass().getSimpleName();

            byType.computeIfAbsent(typeName, k -> new ArrayList<>()).add(fig);
        }

        System.out.println("Figure groups:");
        byType.forEach((type, list) -> {
            double totalArea = list.stream()
                    .mapToDouble(Figure::getArea)
                    .sum();
            System.out.printf("  %-22s count: %d, total area: %.2f%n",
                    type, list.size(), totalArea);
        });


        System.out.println("\n=== 2) UNIQUE FIGURES BY COLOR ===");

        Map<String, Set<Figure>> byColor = new HashMap<>();

        for (Figure fig : figures) {
            byColor.computeIfAbsent(fig.getColor(), k -> new HashSet<>()).add(fig);
        }

        System.out.println("\nVerification: adding duplicate figure to existing Set by color:");
        if (byColor.containsKey("blue")) {
            Square duplicateSquare = new Square(5, "blue");
            int sizeBefore = byColor.get("blue").size();
            byColor.get("blue").add(duplicateSquare);
            int sizeAfter = byColor.get("blue").size();
            System.out.printf("  Set size for 'blue' before: %d, after adding duplicate: %d → %s%n",
                    sizeBefore, sizeAfter,
                    sizeBefore == sizeAfter ? "duplicate rejected ✓" : "duplicate added ✗");
        }

        System.out.println("\nSet size by color:");
        byColor.forEach((color, set) ->
                System.out.printf("  %-8s → %d unique figures%n", color, set.size()));


        System.out.println("\n=== 3) TOP-3 LARGEST FIGURES ===");

        List<Figure> sortedFigures = new ArrayList<>(figures);
        sortedFigures.sort(Comparator.comparingDouble(Figure::getArea).reversed());

        System.out.println("Top-3:");
        sortedFigures.stream()
                .limit(3)
                .forEach(fig -> System.out.printf("  %s [%s] area=%.2f%n",
                        fig.getClass().getSimpleName(),
                        fig.getColor(),
                        fig.getArea()));


        System.out.println("\n=== 4) AVERAGE AREA BY COLOR ===");

        Map<String, double[]> sumAndCount = new HashMap<>();

        for (Figure fig : figures) {
            sumAndCount.merge(
                    fig.getColor(),
                    new double[]{fig.getArea(), 1},
                    (existing, incoming) -> {
                        existing[0] += incoming[0];
                        existing[1] += incoming[1];
                        return existing;
                    }
            );
        }

        System.out.println("Average area by color:");
        sumAndCount.forEach((color, arr) -> {
            double average = arr[0] / arr[1];
            System.out.printf("  %-8s : %.2f%n", color, average);
        });


        System.out.println("\n=== 5) IMMUTABLE CATALOG ===");

        Map<String, List<Figure>> originalMap = byType;
        Map<String, List<Figure>> catalog = Collections.unmodifiableMap(originalMap);

        System.out.println("Catalog size (via wrapper): " + catalog.size());

        System.out.println("\nAttempting catalog.put(\"Test\", ...) through wrapper:");
        try {
            catalog.put("Test", new ArrayList<>());
        } catch (UnsupportedOperationException e) {
            System.out.println("  " + e.getClass().getSimpleName() + " — wrapper is protected from writes!");
        }


        if (!originalMap.isEmpty()) {
            String firstKey = originalMap.keySet().iterator().next();
            originalMap.get(firstKey).add(supplier.getRandomFigure());
        }

        int sizeViaWrapper = catalog.values().stream().mapToInt(List::size).sum();
        int sizeViaOriginal = originalMap.values().stream().mapToInt(List::size).sum();
        System.out.println("\nAfter adding a figure to the original map:");
        System.out.println("  Size via wrapper  : " + sizeViaWrapper);
        System.out.println("  Size via original : " + sizeViaOriginal);
        System.out.println("  Sizes are equal → catalog is a wrapper, not an independent copy!");
    }
}
