package org.example.task3_factory;

public class Main {
    public static void main(String[] args) {
        DatabaseConnectorFactory factory = new DatabaseConnectorFactory();

        // Тестуємо всі три типи баз даних
        System.out.println("=== Testing PostgreSQL ===");
        testConnector(factory, "postgres");

        System.out.println("\n=== Testing MySQL ===");
        testConnector(factory, "mysql");

        System.out.println("\n=== Testing MongoDB ===");
        testConnector(factory, "mongo");

        // Тестуємо обробку помилки
        System.out.println("\n=== Testing Error Handling ===");
        try {
            factory.createConnector("oracle");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void testConnector(DatabaseConnectorFactory factory, String type) {
        DatabaseConnector connector = factory.createConnector(type);
        connector.connect();
        System.out.println("Connected to: " + connector.getDatabaseType());
        connector.disconnect();
    }
}
