package org.example;

public class Circle extends Figure {
    private double radius;

    public Circle(double radius, String color) {
        super(color);

        if (radius <= 0) {
            throw new IllegalArgumentException("Radius must be positive");
        }

        this.radius = radius;
    }

    @Override
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