package ru.flippy.skyscrapers.sdk.listener;

import ru.flippy.skyscrapers.sdk.api.helper.Source;

public interface SourceCallback {
    void onResponse(Source doc);
}
