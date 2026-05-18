package org.example.supplier;

import java.util.Random;

public class ColorSupplier {
    private final String[] colors = {
            "red",
            "blue",
            "green",
            "yellow",
            "black",
            "white"
    };

    private Random random = new Random();

    public String getRandomColor() {
        int index = random.nextInt(colors.length);
        return colors[index];
    }
}
