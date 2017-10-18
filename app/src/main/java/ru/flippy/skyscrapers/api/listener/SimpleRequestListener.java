package ru.flippy.skyscrapers.api.listener;

public interface SimpleRequestListener {

    public void onSuccess();

    public void onError(int errorCode);
}
