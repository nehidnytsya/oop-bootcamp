package org.example;

import org.example.exception.FigureNotFoundException;
import org.example.figure.Figure;
import org.example.storage.FigureStorage;
import org.example.supplier.FigureSupplier;
import org.example.util.AnnotationUtils;

public class Main {
    public static void main(String[] args) {
        FigureStorage<Figure> storage = new FigureStorage<>();
        FigureSupplier supplier = new FigureSupplier();

        storage.add(supplier.getRandomFigure());
        storage.add(supplier.getRandomFigure());
        storage.add(supplier.getDefaultFigure());

        System.out.println("Storage size: " + storage.size());

        System.out.println("\n--- getById ---");
        for (int id = 1; id <= storage.size() + 2; id++) {
            try {
                Figure fig = storage.getById(id);
                System.out.println("ID " + id + ": " + fig.getClass().getSimpleName() +
                        ", area = " + fig.getArea());
            } catch (FigureNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println("\n--- Proving loop survives exception ---");
        System.out.println("Loop completed successfully!");

        System.out.println("\n--- Annotation analysis ---");
        AnnotationUtils.printDefaultAreaMethods(storage);
    }
}