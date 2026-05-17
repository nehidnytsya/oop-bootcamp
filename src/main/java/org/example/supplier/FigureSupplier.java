package org.example.supplier;

import org.example.figure.*;

import java.util.Random;

public class FigureSupplier {
    private Random random = new Random();
    private ColorSupplier colorSupplier = new ColorSupplier();

    public Figure getRandomFigure() {
        int type = random.nextInt(5);

        double a = 1 + random.nextInt(10);
        double b = 1 + random.nextInt(10);
        String color = colorSupplier.getRandomColor();

        switch (type) {
            case 0:
                return new Square(a, color);

            case 1:
                return new Rectangle(a, b, color);

            case 2:
                return new RightTriangle(a, b, color);

            case 3:
                return new Circle(a, color);

            default:
                return new IsoscelesTrapezoid(a, b, 5, color);
        }
    }

    public Figure getDefaultFigure() {
        return new Circle();
    }
}