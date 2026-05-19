package org.example.figure;

public class IsoscelesTrapezoid extends Figure {
    private final double topBase;
    private final double bottomBase;
    private final double height;

    public IsoscelesTrapezoid(double topBase, double bottomBase, double height, String color) {
        super(color);

        if (topBase <= 0 || bottomBase <= 0 || height <= 0) {
            throw new IllegalArgumentException("All parameters must be positive");
        }

        this.topBase = topBase;
        this.bottomBase = bottomBase;
        this.height = height;
    }

    @Override
    public double getArea() {
        return (topBase + bottomBase) * height / 2;
    }

    @Override
    public void draw() {
        System.out.printf(
                "Figure: trapezoid, area: %.1f sq. units, topBase: %.1f units, bottomBase: %.1f units, height: %.1f units, color: %s%n",
                getArea(), topBase, bottomBase, height, getColor()
        );
    }
}
