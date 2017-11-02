package ru.flippy.skyscrapers.sdk.listener;

public interface ActionRequestListener {

    void onSuccess();

    void onError(int errorCode);
}
