package ru.flippy.skyscrapers.sdk.api.methods.blacklist;

public class SkyscrapersBlackList {

    public BlackListRequest get(int page) {
        return new BlackListRequest(page);
    }

    public BlackListAddRequest add(String nick) {
        return new BlackListAddRequest(nick);
    }
}
