package ru.flippy.skyscrapers.sdk.api.methods.mail;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.flippy.skyscrapers.sdk.api.SkyscrapersApi;
import ru.flippy.skyscrapers.sdk.api.helper.FixedSizeList;
import ru.flippy.skyscrapers.sdk.api.helper.Parser;
import ru.flippy.skyscrapers.sdk.api.methods.BaseRequest;
import ru.flippy.skyscrapers.sdk.api.model.Dialog;
import ru.flippy.skyscrapers.sdk.api.model.MiniMessage;
import ru.flippy.skyscrapers.sdk.api.model.Page;
import ru.flippy.skyscrapers.sdk.api.model.Profile;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.api.retrofit.TagCallback;
import ru.flippy.skyscrapers.sdk.listener.RequestListener;
import ru.flippy.skyscrapers.sdk.util.Utils;

public class MailDialogsRequest extends BaseRequest {

    private FixedSizeList<Document> pages;
    private LinkedHashMap<Long, MiniMessage> messages;
    private List<Long> interlocutorIds;
    private FixedSizeList<Dialog> dialogs;

    public void execute(final RequestListener<List<Dialog>> listener) {
        RetrofitClient.getApi().mail().enqueue(new Callback<Page>() {
            @Override
            public void onResponse(Call<Page> call, Response<Page> response) {
                Page page = response.body();
                if (!response.isSuccessful() || page == null) {
                    listener.onError(UNKNOWN);
                } else {
                    Document document = page.getDocument();
                    Element content = document.select("div.m5").first();
                    if (!content.select("div.minor:contains(Почта пустая, писем нет)").isEmpty()) {
                        listener.onResponse(new ArrayList<Dialog>());
                    } else {
                        int pageCount = Parser.from(document).getPageCount(Parser.TYPE_NORMAL);
                        pages = new FixedSizeList<>(pageCount);
                        for (int pageNumber = 0; pageNumber < pageCount; pageNumber++) {
                            TagCallback<Page> callback = new TagCallback<Page>() {
                                @Override
                                public void onResponse(Call<Page> call, Response<Page> response) {
                                    Page page = response.body();
                                    if (!response.isSuccessful() || page == null) {
                                        listener.onError(UNKNOWN);
                                    } else {
                                        pages.set((int) getTag(), page.getDocument());
                                        if (pages.isFilled()) {
                                            messages = new LinkedHashMap<>();
                                            for (Document document : pages) {
                                                Elements messageElements = document.select("div.m5").first().select("div>div:has(span.tdn>span.user)");
                                                //сообщения снизу вверх
                                                for (int i = messageElements.size() - 1; i >= 0; i--) {
                                                    Element messageElement = messageElements.get(i);
                                                    Element userElement = messageElement.select("span.tdn>span.user>a").first();
                                                    Element msgElement = messageElement.select("a[href*=/mail/read/id/]").first();
                                                    MiniMessage message = new MiniMessage();
                                                    message.setId(Utils.getValueAfterLastSlash(msgElement.attr("href")));
                                                    message.setText(msgElement.select("span").first().text());
                                                    message.setOut(messageElement.select("img[src*=letter]").first().attr("src").contains("letterout"));
                                                    message.setRead(msgElement.attr("class").endsWith("minor"));
                                                    long interlocutorId = Utils.getValueAfterLastSlash(userElement.attr("href"));
                                                    messages.put(interlocutorId, message);
                                                }
                                            }
                                            //теперь LinkedHashMap содержит последние сообщения всех диалогов
                                            dialogs = new FixedSizeList<>(messages.size());
                                            interlocutorIds = new ArrayList<>(messages.keySet());
                                            for (Long interlocutorId : interlocutorIds) {
                                                SkyscrapersApi.getProfile(interlocutorId).execute(new RequestListener<Profile>() {
                                                    @Override
                                                    public void onResponse(Profile profile) {
                                                        long profileId = profile.getId();
                                                        MiniMessage lastMessage = messages.get(profileId);
                                                        Dialog dialog = new Dialog();
                                                        dialog.setId(lastMessage.getId()); //dialogId = lastMessageId
                                                        dialog.setLastMessage(lastMessage);
                                                        dialog.setInterlocutor(profile);
                                                        dialogs.set(interlocutorIds.indexOf(profileId), dialog);
                                                        if (dialogs.isFilled()) {
                                                            listener.onResponse(dialogs);
                                                        }
                                                    }

                                                    @Override
                                                    public void onError(int errorCode) {
                                                        listener.onError(NETWORK);
                                                    }
                                                });
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<Page> call, Throwable t) {
                                    listener.onError(NETWORK);
                                }
                            };
                            callback.setTag(pageCount - pageNumber - 1);
                            RetrofitClient.getApi().mailPagination(pageNumber).enqueue(callback);
                        }
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
