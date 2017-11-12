package ru.flippy.skyscrapers.sdk.api.model;

import ru.flippy.skyscrapers.sdk.api.model.time.DateTime;

public class Comment {

    private User author;
    private String message;
    private DateTime sendingDateTime;

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DateTime getSendingDateTime() {
        return sendingDateTime;
    }

    public void setSendingDateTime(DateTime sendingDateTime) {
        this.sendingDateTime = sendingDateTime;
    }
}
