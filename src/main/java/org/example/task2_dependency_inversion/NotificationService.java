package org.example.task2_dependency_inversion;

interface NotificationSender {
    void send(String message);
}

class EmailNotification implements NotificationSender {
    @Override
    public void send(String message) {
        System.out.println("Sending email: " + message);
    }
}

class SmsNotification implements NotificationSender {
    @Override
    public void send(String message) {
        System.out.println("Sending SMS: " + message);
    }
}

class PushNotification implements NotificationSender {
    @Override
    public void send(String message) {
        System.out.println("Sending push notification: " + message);
    }
}
class NotificationService {
    private final NotificationSender sender;

    public NotificationService(NotificationSender sender) {
        this.sender = sender;
    }

    public void notify(String message) {
        sender.send(message);
    }
}
