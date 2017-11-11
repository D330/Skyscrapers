package ru.flippy.skyscrapers.sdk.api.request.friends;

import org.jsoup.nodes.Document;

import java.util.HashMap;

import ru.flippy.skyscrapers.sdk.api.Error;
import ru.flippy.skyscrapers.sdk.api.helper.FormParser;
import ru.flippy.skyscrapers.sdk.api.helper.Parser;
import ru.flippy.skyscrapers.sdk.api.retrofit.DocumentCallback;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;

public class FriendsAddByNickRequest {

    private String nick;

    public FriendsAddByNickRequest(String nick) {
        this.nick = nick;
    }

    public void execute(final ActionRequestListener listener) {
        RetrofitClient.getApi().friends().setErrorPoint(listener).enqueue(new DocumentCallback() {
            @Override
            public void onResponse(Document document, long wicket) {
                if (!FormParser.checkForm(document, "addForm")) {
                    listener.onError(Error.LIMIT);
                } else {
                    HashMap<String, String> postData = FormParser.parse(document)
                            .findByAction("addForm")
                            .input("name", nick)
                            .build();
                    RetrofitClient.getApi().friendsAdd(wicket, postData).setErrorPoint(listener).enqueue(new DocumentCallback() {
                        @Override
                        public void onResponse(Document document, long wicket) {
                            Parser parser = Parser.from(document);
                            if (parser.checkFeedBack(Parser.FEEDBACK_INFO, "в друзья")) {
                                listener.onSuccess();
                            } else if (parser.checkFeedBack(Parser.FEEDBACK_ERROR, "Такой игрок не найден")) {
                                listener.onError(Error.NOT_FOUND);
                            } else {
                                listener.onError(Error.UNKNOWN);
                            }
                        }
                    });
                }
            }
        });
    }
}
