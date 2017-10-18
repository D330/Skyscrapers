package ru.flippy.skyscrapers.api.method;


import ru.flippy.skyscrapers.api.request.friends.FriendsAddByIdRequest;
import ru.flippy.skyscrapers.api.request.friends.FriendsAddByNickRequest;
import ru.flippy.skyscrapers.api.request.friends.FriendsRemoveRequest;
import ru.flippy.skyscrapers.api.request.friends.FriendsRequest;

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
