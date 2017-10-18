package ru.flippy.skyscrapers.api.listener;

public interface OnRequestListener<T> {

    public void onResponse(T response);

    public void onError(int errorCode);
}
