package ru.flippy.skyscrapers.api.listener;

import org.jsoup.nodes.Document;

public interface OnTagDocumentListener {

    public void onResponse(Document document, long wicket, Object tag);

    public void onError();
}