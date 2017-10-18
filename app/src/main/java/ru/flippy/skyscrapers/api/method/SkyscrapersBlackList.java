package ru.flippy.skyscrapers.api.method;

import ru.flippy.skyscrapers.api.request.blacklist.BlackListAddRequest;
import ru.flippy.skyscrapers.api.request.blacklist.BlackListRequest;

public class SkyscrapersBlackList {

    public BlackListRequest get(int page) {
        return new BlackListRequest(page);
    }

    public BlackListAddRequest add(String nick) {
        return new BlackListAddRequest(nick);
    }
}
