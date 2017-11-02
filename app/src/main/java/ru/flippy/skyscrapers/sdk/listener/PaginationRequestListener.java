package ru.flippy.skyscrapers.sdk.listener;

public interface PaginationRequestListener<T> {

    void onResponse(T response, int pageCount);

    void onError(int errorCode);
}
