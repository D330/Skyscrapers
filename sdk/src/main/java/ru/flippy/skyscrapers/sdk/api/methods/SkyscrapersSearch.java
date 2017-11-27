package ru.flippy.skyscrapers.sdk.api.methods;

import ru.flippy.skyscrapers.sdk.api.model.PaginationItem;
import ru.flippy.skyscrapers.sdk.api.request.search.SearchCityRequest;
import ru.flippy.skyscrapers.sdk.api.request.search.SearchNoCityRequest;
import ru.flippy.skyscrapers.sdk.api.request.search.SearchOnlineRequest;
import ru.flippy.skyscrapers.sdk.api.request.search.SearchUserRequest;

public class SkyscrapersSearch {

    public SearchUserRequest user(String nick) {
        return new SearchUserRequest(nick);
    }

    public SearchUserRequest user(PaginationItem paginationItem) {
        return new SearchUserRequest(paginationItem);
    }

    public SearchCityRequest city(String name) {
        return new SearchCityRequest(name);
    }

    public SearchCityRequest city(PaginationItem paginationItem) {
        return new SearchCityRequest(paginationItem);
    }

    public SearchNoCityRequest noCity() {
        return new SearchNoCityRequest();
    }

    public SearchOnlineRequest online() {
        return new SearchOnlineRequest(null);
    }

    public SearchOnlineRequest onlinePagination(PaginationItem paginationItem) {
        return new SearchOnlineRequest(paginationItem);
    }
}
