package ru.flippy.skyscrapers.api.listeners;

public interface ActionRequestListener {

    void onSuccess();

    void onError(int errorCode);
}
