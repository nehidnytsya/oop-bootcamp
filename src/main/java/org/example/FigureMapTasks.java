package org.example;

import org.example.figure.Figure;
import org.example.figure.Square;
import org.example.supplier.FigureSupplier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class FigureMapTasks {

    public static void main(String[] args) {

        FigureSupplier supplier = new FigureSupplier();
        List<Figure> figures = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            figures.add(supplier.getRandomFigure());
        }

        System.out.println("=== GENERATED FIGURES ===");
        figures.forEach(Figure::draw);


        // 1) GROUPING BY TYPE
        System.out.println("\n=== 1) GROUPING BY TYPE ===");

        Map<String, List<Figure>> byType = new HashMap<>();

        for (Figure fig : figures) {
            byType.computeIfAbsent(fig.getClass().getSimpleName(), k -> new ArrayList<>()).add(fig);
        }

        System.out.println("Figure groups:");
        new TreeMap<>(byType).forEach((type, list) -> {
            double totalArea = list.stream()
                    .mapToDouble(Figure::getArea)
                    .sum();
            System.out.printf("  %-22s count: %d, total area: %.2f%n",
                    type, list.size(), totalArea);
        });


        // 2) UNIQUE FIGURES BY COLOR
        System.out.println("\n=== 2) UNIQUE FIGURES BY COLOR ===");

        Map<String, Set<Figure>> byColor = new HashMap<>();

        for (Figure fig : figures) {
            byColor.computeIfAbsent(fig.getColor(), k -> new HashSet<>()).add(fig);
        }

        Set<Figure> blueSet = byColor.computeIfAbsent("blue", k -> new HashSet<>());
        int before = blueSet.size();
        blueSet.add(new Square(5, "blue"));
        blueSet.add(new Square(5, "blue"));
        int after = blueSet.size();
        System.out.printf(
                "%nVerification: adding two equal Square(5, \"blue\") to byColor[\"blue\"] — before: %d, after: %d (expected +1) → %s%n",
                before, after,
                after - before == 1 ? "equals/hashCode OK ✓" : "equals/hashCode BROKEN ✗"
        );

        System.out.println("\nSet size by color:");
        new TreeMap<>(byColor).forEach((color, set) ->
                System.out.printf("  %-8s → %d unique figures%n", color, set.size()));


        // 3) TOP-3 LARGEST FIGURES
        System.out.println("\n=== 3) TOP-3 LARGEST FIGURES ===");

        // list.sort()          — метод інтерфейсу List (з Java 8), делегує до Arrays.sort()
        // Collections.sort()   — статичний утилітний метод, сам делегує до list.sort()
        // Для мене різниці немає обидва стабільні, результат такий самий
        // list.sort() є більш сучасним і читабельним, тому він
        List<Figure> sortedFigures = new ArrayList<>(figures);
        sortedFigures.sort(Comparator.comparingDouble(Figure::getArea).reversed());

        System.out.println("Top-3:");
        sortedFigures.stream()
                .limit(3)
                .forEach(fig -> System.out.printf("  %s [%s] area=%.2f%n",
                        fig.getClass().getSimpleName(),
                        fig.getColor(),
                        fig.getArea()));


        // 4) AVERAGE AREA BY COLOR
        System.out.println("\n=== 4) AVERAGE AREA BY COLOR ===");

        Map<String, double[]> sumAndCount = new HashMap<>();

        for (Figure fig : figures) {
            sumAndCount.merge(
                    fig.getColor(),
                    new double[]{fig.getArea(), 1},
                    (existing, incoming) -> new double[]{
                            existing[0] + incoming[0],
                            existing[1] + incoming[1]
                    }
            );
        }

        System.out.println("Average area by color:");
        new TreeMap<>(sumAndCount).forEach((color, arr) ->
                System.out.printf("  %-8s : %.2f%n", color, arr[0] / arr[1]));


        // 5) IMMUTABLE CATALOG
        System.out.println("\n=== 5) IMMUTABLE CATALOG ===");

        Map<String, List<Figure>> catalog = Collections.unmodifiableMap(byType);

        System.out.println("Catalog size (via wrapper): " + catalog.size());

        System.out.println("\nAttempting catalog.put(\"Test\", ...) through wrapper:");
        try {
            catalog.put("Test", new ArrayList<>());
        } catch (UnsupportedOperationException e) {
            System.out.println("  " + e.getClass().getSimpleName()
                    + " — wrapper blocks all write operations!");
        }

        String firstKey = byType.keySet().iterator().next();
        byType.get(firstKey).add(supplier.getRandomFigure());

        int sizeViaWrapper = catalog.values().stream().mapToInt(List::size).sum();
        int sizeViaOriginal = byType.values().stream().mapToInt(List::size).sum();
        System.out.println("\nAfter adding a figure to the original map:");
        System.out.println("  Size via wrapper  : " + sizeViaWrapper);
        System.out.println("  Size via original : " + sizeViaOriginal);
        System.out.println("  Sizes are equal → catalog is a wrapper, not an independent copy!");
    }
}
