package ru.flippy.skyscrapers.api.method;


import ru.flippy.skyscrapers.api.request.mail.MailDialogHistoryRequest;
import ru.flippy.skyscrapers.api.request.mail.MailDialogsRequest;
import ru.flippy.skyscrapers.api.request.mail.MailMarkAsReadRequest;
import ru.flippy.skyscrapers.api.request.mail.MailRemoveReadRequest;
import ru.flippy.skyscrapers.api.request.mail.MailSendRequest;

public class SkyscrapersMail {

    public MailDialogsRequest getDialogs() {
        return new MailDialogsRequest();
    }

    public MailDialogHistoryRequest getDialogHistory(long dialogId, int page) {
        return new MailDialogHistoryRequest(dialogId, page);
    }

    public MailSendRequest send(long userId, String message) {
        return new MailSendRequest(userId, message);
    }

    public MailMarkAsReadRequest markAsRead() {
        return new MailMarkAsReadRequest();
    }

    public MailRemoveReadRequest removeRead() {
        return new MailRemoveReadRequest();
    }
}