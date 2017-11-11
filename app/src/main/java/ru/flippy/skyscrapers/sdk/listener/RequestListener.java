package ru.flippy.skyscrapers.sdk.listener;

public interface RequestListener<T> extends Errorable {
    void onResponse(T response);
}