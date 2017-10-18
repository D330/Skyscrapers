package ru.flippy.skyscrapers.api.listener;

import org.jsoup.nodes.Document;

public interface OnDocumentListener {

    public void onResponse(Document document, long wicket);

    public void onError();
}
