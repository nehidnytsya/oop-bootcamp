package org.example.figure;

import java.util.Objects;

public abstract class Figure {
    private final String color;

    public Figure(String color) {
        if (color == null) {
            throw new IllegalArgumentException("Color cannot be empty");
        }

        this.color = color;
    }

    public abstract double getArea();
    public abstract void draw();

    public String getColor() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Figure figure = (Figure) o;
        return Objects.equals(color, figure.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color);
    }
}
