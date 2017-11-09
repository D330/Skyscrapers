package ru.flippy.skyscrapers.sdk.api.request.forum;

import org.jsoup.nodes.Document;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.flippy.skyscrapers.sdk.api.helper.FormParser;
import ru.flippy.skyscrapers.sdk.api.helper.Parser;
import ru.flippy.skyscrapers.sdk.api.model.Page;
import ru.flippy.skyscrapers.sdk.api.request.BaseRequest;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;

public class ForumCommentRequest extends BaseRequest {

    public static final int PAGE_NOT_FOUND = 0;
    public static final int TOPIC_CLOSED = 1;
    public static final int BAN = 2;

    private long topicId, replyId;
    private int pageNumber;
    private String message;

    public ForumCommentRequest(long topicId, int page, String message) {
        this.topicId = topicId;
        this.pageNumber = page;
        this.message = message;
    }

    public ForumCommentRequest(long topicId, int page, long replyId, String message) {
        this.topicId = topicId;
        this.pageNumber = page;
        this.replyId = replyId;
        this.message = message;
    }

    public void execute(final ActionRequestListener listener) {
        RetrofitClient.getApi().forumTopic(topicId, pageNumber).enqueue(new Callback<Page>() {
            @Override
            public void onResponse(Call<Page> call, Response<Page> response) {
                Page page = response.body();
                if (!response.isSuccessful() || page == null) {
                    listener.onError(UNKNOWN);
                } else {
                    Document document = page.getDocument();
                    Parser parser = Parser.from(document);
                    if (parser.checkPageError()) {
                        listener.onError(PAGE_NOT_FOUND);
                    } else if (!FormParser.checkForm(document, "StaticPostHandler")) {
                        if (!document.select("a[class=bl amount cntr][href*=bans]:contains(Вы не можете комментировать этот топик)").isEmpty()) {
                            listener.onError(BAN);
                        } else if (!document.select("div[class=cntr amount m5]:contains(топик закрыт)").isEmpty()
                                && !document.select("span[class=bl amount cntr]:contains(Вы не можете комментировать топик, потому что тема закрыта)").isEmpty()) {
                            listener.onError(TOPIC_CLOSED);
                        } else {
                            listener.onError(UNKNOWN);
                        }
                    } else {
                        HashMap<String, String> postData = FormParser.parse(document)
                                .findByAction("StaticPostHandler")
                                .input("staticForm:replyId", replyId)
                                .input("staticForm:text", message)
                                .build();
                        RetrofitClient.getApi().forumComment(topicId, pageNumber, postData).enqueue(new Callback<Page>() {
                            @Override
                            public void onResponse(Call<Page> call, Response<Page> response) {
                                Page page = response.body();
                                if (!response.isSuccessful() || page == null) {
                                    listener.onError(UNKNOWN);
                                } else if (Parser.from(page.getDocument()).checkPageError()) {
                                    listener.onError(UNKNOWN);
                                }
                            }

                            @Override
                            public void onFailure(Call<Page> call, Throwable t) {
                                listener.onError(NETWORK);
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<Page> call, Throwable t) {
                listener.onError(NETWORK);
            }
        });
    }
}
