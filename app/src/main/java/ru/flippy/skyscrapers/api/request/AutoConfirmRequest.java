package ru.flippy.skyscrapers.api.request;

import ru.flippy.skyscrapers.api.helper.DocumentLoader;
import ru.flippy.skyscrapers.api.listener.OnDocumentListener;

public class AutoConfirmRequest extends BaseRequest {

    private long wicket;

    public AutoConfirmRequest(long wicket) {
        this.wicket = wicket;
    }

    public void execute(OnDocumentListener listener) {
        String actionUrl = "http://nebo.mobi/?wicket:interface=:" + wicket + ":confirmLink::ILinkListener::";
        DocumentLoader.connect(actionUrl).execute(listener);
    }
}
