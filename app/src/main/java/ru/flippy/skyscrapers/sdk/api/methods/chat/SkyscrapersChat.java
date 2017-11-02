package ru.flippy.skyscrapers.sdk.api.methods.chat;

public class SkyscrapersChat {

    public ChatSendRequest send(String message) {
        return new ChatSendRequest(message);
    }
}
