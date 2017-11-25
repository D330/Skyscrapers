package ru.flippy.skyscrapers.sdk.api.methods;

import ru.flippy.skyscrapers.sdk.api.model.PaginationItem;
import ru.flippy.skyscrapers.sdk.api.request.rating.RatingCityRequest;
import ru.flippy.skyscrapers.sdk.api.request.rating.RatingUsersRequest;

public class SkyscrapersRating {

    public RatingUsersRequest getUsers(int type) {
        return new RatingUsersRequest(type, null);
    }

    public RatingUsersRequest getUsers(int type, PaginationItem paginationItem) {
        return new RatingUsersRequest(type, paginationItem);
    }

    public RatingCityRequest getCity() {
        return new RatingCityRequest(null);
    }

    public RatingCityRequest getCity(PaginationItem paginationItem) {
        return new RatingCityRequest(paginationItem);
    }

}
