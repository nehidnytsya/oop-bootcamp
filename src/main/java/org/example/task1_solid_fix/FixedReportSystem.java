package org.example.task1_solid_fix;

import java.util.List;


interface ReportGenerator {
    void generate(List<String> data);
}

interface ReportStorage {
    void save(String content);
}

interface ReportSender {
    void send(String content);
}

class PdfReportGenerator implements ReportGenerator {
    @Override
    public void generate(List<String> data) {
        System.out.println("Generating PDF...");
        for (String line : data) {
            System.out.println("[PDF] " + line);
        }
    }
}

class CsvReportGenerator implements ReportGenerator {
    @Override
    public void generate(List<String> data) {
        System.out.println("Generating CSV...");
        for (String line : data) {
            System.out.println(line + ",");
        }
    }
}

class FileReportStorage implements ReportStorage {
    @Override
    public void save(String content) {
        System.out.println("Saving to file: " + content);
    }
}

class EmailReportSender implements ReportSender {
    @Override
    public void send(String content) {
        System.out.println("Sending email with: " + content);
    }
}

class DataValidator {
    public void validate(List<String> data) {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("Data is empty");
        }
    }
}

class ReportProcessor {
    private final ReportGenerator generator;
    private final ReportStorage storage;
    private final ReportSender sender;
    private final DataValidator validator;

    public ReportProcessor(ReportGenerator generator,
                           ReportStorage storage,
                           ReportSender sender,
                           DataValidator validator) {
        this.generator = generator;
        this.storage = storage;
        this.sender = sender;
        this.validator = validator;
    }

    public void processReport(List<String> data) {
        validator.validate(data);
        generator.generate(data);
        String content = String.join("\n", data);
        storage.save(content);
        sender.send(content);
    }
}
