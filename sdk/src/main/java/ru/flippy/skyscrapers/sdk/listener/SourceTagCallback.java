package ru.flippy.skyscrapers.sdk.listener;

import ru.flippy.skyscrapers.sdk.api.helper.Source;

public interface SourceTagCallback {
    void onResponse(Object tag, Source doc);
}
