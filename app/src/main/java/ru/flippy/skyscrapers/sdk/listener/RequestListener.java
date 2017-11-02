package ru.flippy.skyscrapers.sdk.listener;

public interface RequestListener<T> {

    void onResponse(T response);

    void onError(int errorCode);
}
