package ru.flippy.skyscrapers.sdk.api.methods.mail;

public class SkyscrapersMail {

    public MailDialogHistoryRequest getDialogHistory(long userId, int page) {
        return new MailDialogHistoryRequest(userId, page);
    }

    public MailDialogsRequest getDialogs() {
        return new MailDialogsRequest();
    }

    public MailMarkAsReadRequest markAsRead() {
        return new MailMarkAsReadRequest();
    }

    public MailDeleteReadRequest removeRead() {
        return new MailDeleteReadRequest();
    }

    public MailSendRequest send(long userId, String message) {
        return new MailSendRequest(userId, message);
    }
}
