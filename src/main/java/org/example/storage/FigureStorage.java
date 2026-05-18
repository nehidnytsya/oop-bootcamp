package org.example.storage;

import org.example.exception.FigureNotFoundException;
import org.example.figure.Figure;

import java.util.ArrayList;
import java.util.List;

public class FigureStorage<T extends Figure> {
    private final List<T> figures = new ArrayList<>();

    public void add(T figure) {
        figures.add(figure);
    }

    public T getById(int id) {
        if (id < 0 || id >= figures.size()) {
            throw new FigureNotFoundException("Figure #" + id + " not found");
        }
        return figures.get(id);
    }

    public int size() {
        return figures.size();
    }

    public List<T> getAll() {
        return new ArrayList<>(figures);
    }
}
