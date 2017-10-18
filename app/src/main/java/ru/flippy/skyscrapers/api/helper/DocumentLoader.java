package ru.flippy.skyscrapers.api.helper;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import ru.flippy.skyscrapers.api.listener.OnDocumentListener;
import ru.flippy.skyscrapers.api.listener.OnTagDocumentListener;

public class DocumentLoader {

    public DocumentLoader() {
    }

    private ANRequest request;

    public static DocumentLoader connect(String url) {
        DocumentLoader loader = new DocumentLoader();
        loader.request = AndroidNetworking.get(url).build();
        return loader;
    }

    public void execute(final OnDocumentListener callback) {
        request.getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String response) {
                Document document = Jsoup.parse(response);
                ExtremeParser parser = new ExtremeParser(document);
                callback.onResponse(document, parser.getWicket());
            }

            @Override
            public void onError(ANError anError) {
                callback.onError();
            }
        });
    }

    public void executeWithTag(final Object tag, final OnTagDocumentListener tagableCallback) {
        request.getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String response) {
                Document document = Jsoup.parse(response);
                ExtremeParser parser = new ExtremeParser(document);
                tagableCallback.onResponse(document, parser.getWicket(), tag);
            }

            @Override
            public void onError(ANError anError) {
                tagableCallback.onError();
            }
        });
    }
}
