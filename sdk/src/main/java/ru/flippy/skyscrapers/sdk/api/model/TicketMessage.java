package ru.flippy.skyscrapers.sdk.api.model;

import ru.flippy.skyscrapers.sdk.api.model.time.DateTime;

public class TicketMessage {

    private String text;
    private DateTime sendingDateTime;
    private boolean out;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public DateTime getSendingDateTime() {
        return sendingDateTime;
    }

    public void setSendingDateTime(DateTime sendingDateTime) {
        this.sendingDateTime = sendingDateTime;
    }

    public boolean isOut() {
        return out;
    }

    public void setOut(boolean out) {
        this.out = out;
    }
}
