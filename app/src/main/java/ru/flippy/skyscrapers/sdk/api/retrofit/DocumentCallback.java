package ru.flippy.skyscrapers.sdk.api.retrofit;

import org.jsoup.nodes.Document;

public interface DocumentCallback {
    void onResponse(Document document, long wicket);
}
