package org.example;

public class Square extends Figure {
    private double side;

    public Square(double side, String color) {
        super(color);

        if (side <= 0) {
            throw new IllegalArgumentException("Side must be positive");
        }

        this.side = side;
    }

    @Override
    public double getArea() {
        return side * side;
    }

    @Override
    public void draw() {
        System.out.printf(
                "Figure: square, area: %.1f sq. units, side: %.1f units, color: %s%n",
                getArea(), side, getColor()
        );
    }
}