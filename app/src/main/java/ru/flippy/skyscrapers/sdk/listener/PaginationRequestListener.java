package ru.flippy.skyscrapers.sdk.listener;

public interface PaginationRequestListener<T> extends Errorable {
    void onResponse(T response, int pageCount);
}
