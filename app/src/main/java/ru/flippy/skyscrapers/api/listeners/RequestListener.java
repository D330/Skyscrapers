package ru.flippy.skyscrapers.api.listeners;

public interface RequestListener<T> {

    void onResponse(T response);

    void onError(int errorCode);
}
