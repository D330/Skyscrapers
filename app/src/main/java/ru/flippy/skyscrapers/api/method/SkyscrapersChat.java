package ru.flippy.skyscrapers.api.method;

import ru.flippy.skyscrapers.api.request.chat.ChatSendRequest;

public class SkyscrapersChat {

    public ChatSendRequest send(String message) {
        return new ChatSendRequest(message);
    }
}
