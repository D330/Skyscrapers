package ru.flippy.skyscrapers.api.helper;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.FormElement;

import java.util.List;

import ru.flippy.skyscrapers.api.listener.OnDocumentListener;
import ru.flippy.skyscrapers.api.listener.OnTagDocumentListener;

public class FormParser {

    private FormParser() {
    }

    public static boolean checkForm(Document document, String actionContains) {
        return !document.select("form[action*=" + actionContains + "]").isEmpty();
    }

    public static Builder parse(Document document) {
        return new FormParser().new Builder(document);
    }

    public class Builder {

        private Document document;
        private List<Connection.KeyVal> formData;
        private ANRequest request;

        private Builder(Document document) {
            this.document = document;
        }

        public Builder findByAction(String actionContains) {
            formData = ((FormElement) document.select("form[action*=" + actionContains + "]").first()).formData();
            return this;
        }

        public Builder input(String inputKey, String inputValue) {
            for (int i = 0; i < formData.size(); i++) {
                Connection.KeyVal keyVal = formData.get(i);
                if (keyVal.key().equals(inputKey)) {
                    formData.set(i, keyVal.value(inputValue));
                }
            }
            return this;
        }

        public Builder connect(String url) {
            request = AndroidNetworking.post(url).addBodyParameter(formData).build();
            return this;
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
}
