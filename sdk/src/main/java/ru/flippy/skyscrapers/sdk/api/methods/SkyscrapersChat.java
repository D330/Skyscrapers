package ru.flippy.skyscrapers.sdk.api.methods;

import ru.flippy.skyscrapers.sdk.api.request.chat.ChatSendRequest;

public class SkyscrapersChat {

    public ChatSendRequest send(String message) {
        return new ChatSendRequest(message);
    }
}
