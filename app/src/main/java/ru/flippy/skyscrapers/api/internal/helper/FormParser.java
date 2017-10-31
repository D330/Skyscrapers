package ru.flippy.skyscrapers.api.internal.helper;

import android.support.annotation.CheckResult;
import android.util.Log;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.FormElement;

import java.util.HashMap;
import java.util.List;

import ru.flippy.skyscrapers.api.SkyscrapersSDK;

public class FormParser {

    private static final String LOG_TAG = FormParser.class.getSimpleName();

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
        private HashMap<String, String> postData;

        private Builder(Document document) {
            this.document = document;
        }

        public Builder findByAction(String actionContains) {
            FormElement form = (FormElement) document.select("form[action*=" + actionContains + "]").first();
            List<Connection.KeyVal> formData = form.formData();
            postData = new HashMap<>(formData.size());
            return this;
        }

        public Builder input(String key, String value) {
            postData.put(key, value);
            return this;
        }

        @CheckResult
        public HashMap<String, String> build() {
            for (String k : postData.keySet()) {
                Log.d(LOG_TAG, k + ": " + postData.get(k));
            }
            return postData;
        }
    }
}
