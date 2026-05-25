package org.example.task1_solid_fix;

import java.util.List;

public class ReportManager {
    private String reportType;
    public ReportManager(String reportType) {
        this.reportType = reportType;
    }
    public void generateReport(List<String> data) {
        if (reportType.equals("PDF")) {
            System.out.println("Generating PDF...");
            for (String line : data) {
                System.out.println("[PDF] " + line);
            }
        } else if (reportType.equals("CSV")) {
            System.out.println("Generating CSV...");
            for (String line : data) {
                System.out.println(line + ",");
            }
        }
    }
    public void saveToFile(String content) {
        System.out.println("Saving to file: " + content);
    }
    public void sendByEmail(String content) {
        System.out.println("Sending email with: " + content);
    }
    public void validateData(List<String> data) {
        if (data == null || data.isEmpty()) {
            throw new RuntimeException("Data is empty");
        }
    }
}
