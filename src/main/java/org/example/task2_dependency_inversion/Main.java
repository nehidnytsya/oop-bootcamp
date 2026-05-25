package org.example.task2_dependency_inversion;

public class Main {
    public static void main(String[] args) {
        // Створюємо різні сервіси для різних типів нотифікацій
        NotificationService emailService = new NotificationService(new EmailNotification());
        NotificationService smsService = new NotificationService(new SmsNotification());
        NotificationService pushService = new NotificationService(new PushNotification());

        // Відправляємо повідомлення через різні канали
        emailService.notify("Hello via Email!");
        smsService.notify("Hello via SMS!");
        pushService.notify("Hello via Push Notification!");

        System.out.println("\n=== Додавання нового типу (Slack) без зміни існуючого коду ===");

        // Можемо легко додати Slack без зміни NotificationService
        NotificationSender slackSender = new NotificationSender() {
            @Override
            public void send(String message) {
                System.out.println("Sending Slack message: " + message);
            }
        };

        NotificationService slackService = new NotificationService(slackSender);
        slackService.notify("Hello via Slack!");
    }
}
