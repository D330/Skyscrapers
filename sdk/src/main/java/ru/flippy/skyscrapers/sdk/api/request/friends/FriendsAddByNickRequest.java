package ru.flippy.skyscrapers.sdk.api.request.friends;

import java.util.HashMap;

import ru.flippy.skyscrapers.sdk.api.Error;
import ru.flippy.skyscrapers.sdk.api.helper.FormParser;
import ru.flippy.skyscrapers.sdk.api.model.Feedback;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;

public class FriendsAddByNickRequest {

    private String nick;

    public FriendsAddByNickRequest(String nick) {
        this.nick = nick;
    }

    public void execute(final ActionRequestListener listener) {
        RetrofitClient.getApi().friends()
                .error(listener)
                .success(friendsDoc -> {
                    if (!friendsDoc.hasForm("addForm")) {
                        listener.onError(Error.LIMIT);
                    } else {
                        HashMap<String, String> postData = FormParser.parse(friendsDoc)
                                .findByAction("addForm")
                                .input("name", nick)
                                .build();
                        RetrofitClient.getApi().friendsAdd(friendsDoc.wicket(), postData)
                                .error(listener)
                                .success(resultDoc -> {
                                    if (resultDoc.hasFeedBack(Feedback.Type.INFO, "в друзья")) {
                                        listener.onSuccess();
                                    } else if (resultDoc.hasFeedBack(Feedback.Type.ERROR, "Такой игрок не найден")) {
                                        listener.onError(Error.NOT_FOUND);
                                    } else {
                                        listener.onError(Error.UNKNOWN);
                                    }
                                });
                    }
                });
    }
}
