package org.example.figure;

import org.example.annotation.DefaultArea;

public class Circle extends Figure {
    public static final double DEFAULT_RADIUS = 10.0;
    public static final String DEFAULT_COLOR = "white";

    private final double radius;

    public Circle() {
        this(DEFAULT_RADIUS, DEFAULT_COLOR);
    }

    public Circle(double radius, String color) {
        super(color);

        if (radius <= 0) {
            throw new IllegalArgumentException("Radius must be positive");
        }

        this.radius = radius;
    }

    @Override
    @DefaultArea // Circle is the default figure returned by FigureSupplier.getDefaultFigure()
    public double getArea() {
        return Math.PI * radius * radius;
    }

    @Override
    public void draw() {
        System.out.printf(
                "Figure: circle, area: %.1f sq. units, radius: %.1f units, color: %s%n",
                getArea(), radius, getColor()
        );
    }
}
