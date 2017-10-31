package ru.flippy.skyscrapers.api.internal.retrofit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TagCallback<T> implements Callback<T> {

    private Object tag;

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {

    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {

    }
}
