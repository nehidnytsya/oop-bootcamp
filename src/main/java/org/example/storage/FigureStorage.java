package org.example.storage;

import org.example.exception.FigureNotFoundException;
import org.example.figure.Figure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FigureStorage<T extends Figure> {
    private List<T> storage;
    private Map<Integer, T> idMap;
    private int nextId;

    public FigureStorage() {
        this.storage = new ArrayList<>();
        this.idMap = new HashMap<>();
        this.nextId = 1;
    }

    public void add(T figure) {
        storage.add(figure);
        idMap.put(nextId++, figure);
    }

    public T getById(int id) {
        T figure = idMap.get(id);
        if (figure == null) {
            throw new FigureNotFoundException("Figure #" + id + " not found");
        }
        return figure;
    }

    public int size() {
        return storage.size();
    }

    public List<T> getAll() {
        return new ArrayList<>(storage);
    }
}
