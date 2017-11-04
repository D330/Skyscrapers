package ru.flippy.skyscrapers.sdk.api.methods;

import ru.flippy.skyscrapers.sdk.api.request.mail.MailDeleteReadRequest;
import ru.flippy.skyscrapers.sdk.api.request.mail.MailDialogHistoryRequest;
import ru.flippy.skyscrapers.sdk.api.request.mail.MailDialogsRequest;
import ru.flippy.skyscrapers.sdk.api.request.mail.MailMarkAsReadRequest;
import ru.flippy.skyscrapers.sdk.api.request.mail.MailSendRequest;

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
