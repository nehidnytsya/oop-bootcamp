package org.example.task4_custom_pattern;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Stock Market Monitoring System ===");
        System.out.println("Pattern: Observer\n");

        // Створюємо фондовий ринок (суб'єкт)
        StockMarket market = new StockMarket();

        // Створюємо спостерігачів
        StockObserver investor1 = new InvestorAlert("Alice", 100.0);
        StockObserver investor2 = new InvestorAlert("Bob", 95.0);
        StockAnalytics analytics = new StockAnalytics();

        // Реєструємо спостерігачів
        market.addObserver(investor1);
        market.addObserver(investor2);
        market.addObserver(analytics);

        // Симулюємо зміни цін акцій
        System.out.println("--- Initial prices ---");
        market.updateStockPrice("AAPL", 150.0);
        market.updateStockPrice("GOOGL", 140.0);

        System.out.println("\n--- Price changes ---");
        market.updateStockPrice("AAPL", 98.0);  // Нижче порогу Alice
        market.updateStockPrice("GOOGL", 93.0);  // Нижче порогу Bob

        System.out.println("\n--- Recovery ---");
        market.updateStockPrice("AAPL", 105.0);  // Вище порогу Alice

        // Показуємо аналітику
        System.out.println("\n--- Analytics Report ---");
        System.out.printf("Average AAPL price: $%.2f%n",
                analytics.getAveragePrice("AAPL"));
        System.out.printf("Average GOOGL price: $%.2f%n",
                analytics.getAveragePrice("GOOGL"));
    }
}
