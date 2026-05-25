package org.example.task4_custom_pattern;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

interface StockObserver {
    void update(String stockSymbol, double price);
}

// Суб'єкт (Subject) - фондовий ринок
class StockMarket {
    private Map<String, Double> stocks = new HashMap<>();
    private List<StockObserver> observers = new ArrayList<>();

    public void addObserver(StockObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(StockObserver observer) {
        observers.remove(observer);
    }

    public void updateStockPrice(String symbol, double price) {
        stocks.put(symbol, price);
        notifyObservers(symbol, price);
    }

    private void notifyObservers(String symbol, double price) {
        for (StockObserver observer : observers) {
            observer.update(symbol, price);
        }
    }
}

// Конкретний спостерігач - сповіщення інвестора
class InvestorAlert implements StockObserver {
    private String investorName;
    private double alertThreshold;

    public InvestorAlert(String investorName, double alertThreshold) {
        this.investorName = investorName;
        this.alertThreshold = alertThreshold;
    }

    @Override
    public void update(String stockSymbol, double price) {
        if (price < alertThreshold) {
            System.out.printf("ALERT for %s: %s dropped to $%.2f (below $%.2f)%n",
                    investorName, stockSymbol, price, alertThreshold);
        } else {
            System.out.printf("%s: %s is now $%.2f (OK)%n",
                    investorName, stockSymbol, price);
        }
    }
}

// Конкретний спостерігач - аналітика
class StockAnalytics implements StockObserver {
    private Map<String, List<Double>> priceHistory = new HashMap<>();

    @Override
    public void update(String stockSymbol, double price) {
        priceHistory.computeIfAbsent(stockSymbol, k -> new ArrayList<>()).add(price);

        List<Double> history = priceHistory.get(stockSymbol);
        if (history.size() > 1) {
            double oldPrice = history.get(history.size() - 2);
            double change = ((price - oldPrice) / oldPrice) * 100;
            System.out.printf("ANALYTICS: %s change: %.2f%%%n", stockSymbol, change);
        }
    }

    public double getAveragePrice(String symbol) {
        List<Double> history = priceHistory.get(symbol);
        if (history == null || history.isEmpty()) return 0;
        return history.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0);
    }
}

