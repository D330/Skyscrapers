package ru.flippy.skyscrapers.sdk.listener;

import ru.flippy.skyscrapers.sdk.api.model.Pagination;

public interface PaginationRequestListener<T> extends ErrorListener {
    void onResponse(T response, Pagination pagination);
}
