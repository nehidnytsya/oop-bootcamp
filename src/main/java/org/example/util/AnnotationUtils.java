package org.example.util;

import org.example.annotation.DefaultArea;
import org.example.figure.Figure;
import org.example.storage.FigureStorage;

import java.lang.reflect.Method;
import java.util.*;

public class AnnotationUtils {
    public static void printDefaultAreaMethods(Collection<? extends Figure> figures) {
        System.out.println("\n=== Methods marked with @DefaultArea ===");

        Set<Class<?>> processedClasses = new HashSet<>();

        for (Figure figure : figures) {
            Class<?> clazz = figure.getClass();
            if (processedClasses.add(clazz)) {
                try {
                    Method method = clazz.getMethod("getArea");
                    System.out.println(method.isAnnotationPresent(DefaultArea.class) ?
                            " + " + clazz.getSimpleName() + ".getArea() is DEFAULT" :
                            " - " + clazz.getSimpleName() + ".getArea() is NOT default");
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException("Unexpected: getArea() not found in " + clazz.getName(), e);
                }
            }
        }
    }

    public static void printDefaultAreaMethods(FigureStorage<? extends Figure> storage) {
        printDefaultAreaMethods(storage.getAll());
    }
}