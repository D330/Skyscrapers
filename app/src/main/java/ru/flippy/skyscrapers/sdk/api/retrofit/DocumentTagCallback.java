package ru.flippy.skyscrapers.sdk.api.retrofit;

import org.jsoup.nodes.Document;

public interface DocumentTagCallback {
    void onResponse(Object tag, Document document, long wicket);
}
