package ru.flippy.skyscrapers.api.request.mail;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import ru.flippy.skyscrapers.api.SkyscrapersAPI;
import ru.flippy.skyscrapers.api.Utils;
import ru.flippy.skyscrapers.api.helper.DocumentLoader;
import ru.flippy.skyscrapers.api.helper.ExtremeParser;
import ru.flippy.skyscrapers.api.helper.FixedSizeList;
import ru.flippy.skyscrapers.api.listener.OnDocumentListener;
import ru.flippy.skyscrapers.api.listener.OnRequestListener;
import ru.flippy.skyscrapers.api.listener.OnTagDocumentListener;
import ru.flippy.skyscrapers.api.model.Dialog;
import ru.flippy.skyscrapers.api.model.MiniMessage;
import ru.flippy.skyscrapers.api.model.Profile;
import ru.flippy.skyscrapers.api.request.BaseRequest;

public class MailDialogsRequest extends BaseRequest {

    private FixedSizeList<Document> pages;
    private LinkedHashMap<Long, MiniMessage> messages;
    private List<Long> interlocutorIds;
    private FixedSizeList<Dialog> dialogs;

    public void execute(final OnRequestListener<List<Dialog>> listener) {
        DocumentLoader.connect("http://nebo.mobi/mail").execute(new OnDocumentListener() {
            @Override
            public void onResponse(Document document, long wicket) {
                Element content = document.select("div.m5").first();
                if (!content.select("div.minor:contains(Почта пустая, писем нет)").isEmpty()) {
                    listener.onResponse(new ArrayList<Dialog>());
                } else {
                    ExtremeParser parser = new ExtremeParser(document);
                    int pageCount = parser.getPageCount(ExtremeParser.TYPE_NORMAL);
                    pages = new FixedSizeList<>(pageCount);
                    for (int page = 0; page < pageCount; page++) {
                        DocumentLoader.connect("http://nebo.mobi/mail/page/" + page)
                                .executeWithTag(pageCount - page - 1, new OnTagDocumentListener() {
                                    @Override
                                    public void onResponse(Document document, long wicket, Object tag) {
                                        pages.set((int) tag, document);
                                        if (pages.isFilled()) {
                                            messages = new LinkedHashMap<>();
                                            for (Document page : pages) {
                                                Elements messageElements = page.select("div.m5").first().select("div>div:has(span.tdn>span.user)");
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
                                                SkyscrapersAPI.getProfile(interlocutorId).execute(new OnRequestListener<Profile>() {
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

                                    @Override
                                    public void onError() {
                                        listener.onError(NETWORK);
                                    }
                                });
                    }
                }
            }

            @Override
            public void onError() {
                listener.onError(NETWORK);
            }
        });
    }
}
