package ru.flippy.skyscrapers.sdk.api.model;

import java.util.List;

public class Ticket {

    public static class Type {
        public static final String COMMON = "common";
        public static final String PAYMENT = "payment";
        public static final String SUGGESTION = "suggestion";
    }

    public static class Status {
        public static final int OPEN = 0;
        public static final int WAIT = 1;
        public static final int SOLVED = 2;
        public static final int CLOSED = 3;
    }

    private long id;
    private int status;
    private List<TicketMessage> messages;
    private boolean rateAvailable;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<TicketMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<TicketMessage> messages) {
        this.messages = messages;
    }

    public boolean isRateAvailable() {
        return rateAvailable;
    }

    public void setRateAvailable(boolean rateAvailable) {
        this.rateAvailable = rateAvailable;
    }
}
