package org.example.task1_solid_fix;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        /*
         * ПОРУШЕННЯ SOLID В ОРИГІНАЛЬНОМУ КОДІ:
         *
         * 1. Single Responsibility (S) - клас робить забагато:
         *    - генерує різні типи звітів
         *    - зберігає файли
         *    - відправляє email
         *    - валідує дані
         *
         * 2. Open/Closed (O) - щоб додати новий тип звіту,
         *    потрібно модифікувати існуючий код (додавати новий if-else)
         *
         * 3. Dependency Inversion (DI) клас залежить від
         *    конкретних реалізацій, а не абстракцій
         */

        List<String> data = Arrays.asList("Name,Age", "John,30", "Jane,25");

        System.out.println("=== PDF Report ===");
        ReportProcessor pdfProcessor = new ReportProcessor(
                new PdfReportGenerator(),
                new FileReportStorage(),
                new EmailReportSender(),
                new DataValidator()
        );
        pdfProcessor.processReport(data);

        System.out.println("\n=== CSV Report ===");
        ReportProcessor csvProcessor = new ReportProcessor(
                new CsvReportGenerator(),
                new FileReportStorage(),
                new EmailReportSender(),
                new DataValidator()
        );
        csvProcessor.processReport(data);
    }
}
