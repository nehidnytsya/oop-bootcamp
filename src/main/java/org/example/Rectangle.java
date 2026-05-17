package org.example;

public class Rectangle extends Figure {
    private double width;
    private double height;

    public Rectangle(double width, double height, String color) {
        super(color);

        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Width and height must be positive");
        }

        this.width = width;
        this.height = height;
    }

    @Override
    public double getArea() {
        return width * height;
    }

    @Override
    public void draw() {
        System.out.printf(
                "Figure: rectangle, area: %.1f sq. units, width: %.1f units, height: %.1f units, color: %s%n",
                getArea(), width, height, getColor()
        );
    }
}