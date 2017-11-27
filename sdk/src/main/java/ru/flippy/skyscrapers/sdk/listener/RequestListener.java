package ru.flippy.skyscrapers.sdk.listener;

public interface RequestListener<T> extends ErrorListener {
    void onResponse(T response);
}