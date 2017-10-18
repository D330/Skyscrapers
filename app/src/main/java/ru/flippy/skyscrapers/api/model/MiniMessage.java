package ru.flippy.skyscrapers.api.model;

public class MiniMessage {

    private long id;
    private String text;
    private boolean out, read;

    public void setId(long id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setOut(boolean out) {
        this.out = out;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public boolean isOut() {
        return out;
    }

    public boolean isRead() {
        return read;
    }
}
