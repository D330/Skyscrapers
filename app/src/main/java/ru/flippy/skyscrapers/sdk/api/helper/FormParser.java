package ru.flippy.skyscrapers.sdk.api.helper;

import android.support.annotation.CheckResult;

import org.jsoup.Connection;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;

import java.util.HashMap;
import java.util.List;

public class FormParser {

    private Source doc;
    private HashMap<String, String> postData;

    public static FormParser parse(Source doc) {
        FormParser instance = new FormParser();
        instance.doc = doc;
        return instance;
    }

    public FormParser findByAction(String actionContains) {
        Element form = doc.select("form[action*=" + actionContains + "]").first();
        List<Connection.KeyVal> formData = ((FormElement) form).formData();
        postData = new HashMap<>(formData.size());
        for (Connection.KeyVal formInput : formData) {
            postData.put(formInput.key(), formInput.value());
        }
        return this;
    }

    public FormParser input(String key, Object value) {
        postData.put(key, String.valueOf(value));
        return this;
    }

    @CheckResult
    public HashMap<String, String> build() {
        return postData;
    }
}
