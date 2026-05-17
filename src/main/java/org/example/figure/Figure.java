package org.example.figure;

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
}