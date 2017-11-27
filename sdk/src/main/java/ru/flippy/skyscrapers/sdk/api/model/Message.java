package ru.flippy.skyscrapers.sdk.api.model;

import ru.flippy.skyscrapers.sdk.api.model.time.DateTime;

public class Message extends MiniMessage {

    private User author;
    private DateTime sendingDateTime;

    public void setAuthor(User author) {
        this.author = author;
    }

    public void setSendingDateTime(DateTime sendingDateTime) {
        this.sendingDateTime = sendingDateTime;
    }

    public User getAuthor() {
        return author;
    }

    public DateTime getSendingDateTime() {
        return sendingDateTime;
    }
}
