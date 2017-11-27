package ru.flippy.skyscrapers.sdk.api.methods;

import ru.flippy.skyscrapers.sdk.api.request.blacklist.BlackListAddRequest;
import ru.flippy.skyscrapers.sdk.api.request.blacklist.BlackListRequest;

public class SkyscrapersBlackList {

    public BlackListRequest get(int page) {
        return new BlackListRequest(page);
    }

    public BlackListAddRequest add(String nick) {
        return new BlackListAddRequest(nick);
    }
}
