package org.example;

import org.example.exception.FigureNotFoundException;
import org.example.figure.Figure;
import org.example.storage.FigureStorage;
import org.example.supplier.FigureSupplier;
import org.example.util.AnnotationUtils;

public class Generics {
    public static void main(String[] args) {
        FigureStorage<Figure> storage = new FigureStorage<>();
        FigureSupplier supplier = new FigureSupplier();

        storage.add(supplier.getRandomFigure());
        storage.add(supplier.getRandomFigure());
        storage.add(supplier.getDefaultFigure());

        System.out.println("Storage size: " + storage.size());

        System.out.println("\n--- getById (loop survives FigureNotFoundException) ---");
        int[] idsToTry = {0, 1, 2, 42, 3, 999};
        for (int id : idsToTry) {
            try {
                Figure fig = storage.getById(id);
                System.out.println("ID " + id + ": " + fig.getClass().getSimpleName() +
                        ", area = " + fig.getArea());
            } catch (FigureNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println("Loop completed successfully!");

        System.out.println("\n--- Annotation analysis ---");
        AnnotationUtils.printDefaultAreaMethods(storage);
    }
}
