package ru.flippy.skyscrapers.sdk.api.model;

public class Dialog {

    private long id;
    private User interlocutor;
    private MiniMessage lastMessage;
    private int totalCount, unreadCount;

    public void setId(long id) {
        this.id = id;
    }

    public void setInterlocutor(User interlocutor) {
        this.interlocutor = interlocutor;
    }

    public void setLastMessage(MiniMessage lastMessage) {
        this.lastMessage = lastMessage;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public long getId() {
        return id;
    }

    public User getInterlocutor() {
        return interlocutor;
    }

    public MiniMessage getLastMessage() {
        return lastMessage;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getUnreadCount() {
        return unreadCount;
    }


}
