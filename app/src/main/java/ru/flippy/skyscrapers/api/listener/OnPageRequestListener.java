package ru.flippy.skyscrapers.api.listener;

public interface OnPageRequestListener<T> {

    public void onResponse(T response, int pageCount);

    public void onError(int errorCode);
}
