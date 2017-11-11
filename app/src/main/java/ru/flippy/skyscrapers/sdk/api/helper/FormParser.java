package ru.flippy.skyscrapers.sdk.api.helper;

import android.support.annotation.CheckResult;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.FormElement;

import java.util.HashMap;
import java.util.List;

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

        public Builder input(String key, Object value) {
            postData.put(key, String.valueOf(value));
            return this;
        }

        @CheckResult
        public HashMap<String, String> build() {
            return postData;
        }
    }
}
