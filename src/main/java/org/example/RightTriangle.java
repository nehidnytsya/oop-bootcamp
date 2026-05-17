package org.example;

public class RightTriangle extends Figure {
    private double firstLeg;
    private double secondLeg;

    public RightTriangle(double firstLeg, double secondLeg, String color) {
        super(color);

        if (firstLeg <= 0 || secondLeg <= 0) {
            throw new IllegalArgumentException("Legs must be positive");
        }

        this.firstLeg = firstLeg;
        this.secondLeg = secondLeg;
    }

    @Override
    public double getArea() {
        return firstLeg * secondLeg / 2;
    }

    @Override
    public void draw() {
        System.out.printf(
                "Figure: triangle, area: %.1f sq. units, firstLeg: %.1f units, secondLeg: %.1f units, color: %s%n",
                getArea(), firstLeg, secondLeg, getColor()
        );
    }
}