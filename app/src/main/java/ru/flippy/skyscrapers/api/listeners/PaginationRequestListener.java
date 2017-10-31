package ru.flippy.skyscrapers.api.listeners;

public interface PaginationRequestListener<T> {

    void onResponse(T response, int pageCount);

    void onError(int errorCode);
}
