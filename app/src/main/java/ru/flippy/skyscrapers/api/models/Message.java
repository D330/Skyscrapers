package ru.flippy.skyscrapers.api.models;

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
