package org.example.figure;

import java.util.Objects;

public class RightTriangle extends Figure {
    private final double firstLeg;
    private final double secondLeg;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RightTriangle that = (RightTriangle) o;
        return Double.compare(firstLeg, that.firstLeg) == 0 &&
                Double.compare(secondLeg, that.secondLeg) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), firstLeg, secondLeg);
    }
}
