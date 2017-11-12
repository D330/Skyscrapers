package ru.flippy.skyscrapers.sdk.listener;

import java.util.List;

import ru.flippy.skyscrapers.sdk.api.model.Pagination;

public interface PaginationRequestListener<T> extends Errorable {
    void onResponse(T response, Pagination pagination);
}
