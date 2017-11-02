package ru.flippy.skyscrapers.sdk.api.model;

import org.jsoup.nodes.Document;

public class Page {

    private Document document;
    private long wicket;

    public Page(Document document, long wicket) {
        this.document = document;
        this.wicket = wicket;
    }

    public Document getDocument() {
        return document;
    }

    public long getWicket() {
        return wicket;
    }
}
