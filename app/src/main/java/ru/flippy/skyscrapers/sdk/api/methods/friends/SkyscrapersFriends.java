package ru.flippy.skyscrapers.sdk.api.methods.friends;

public class SkyscrapersFriends {

    public FriendsRequest get(int page) {
        return new FriendsRequest(page);
    }

    public FriendsAddByIdRequest add(long userId) {
        return new FriendsAddByIdRequest(userId);
    }

    public FriendsAddByNickRequest add(String nick) {
        return new FriendsAddByNickRequest(nick);
    }

    public FriendsRemoveRequest remove(long userId) {
        return new FriendsRemoveRequest(userId);
    }
}
