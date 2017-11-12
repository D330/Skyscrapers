package ru.flippy.skyscrapers.application.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import ru.flippy.skyscrapers.R;
import ru.flippy.skyscrapers.sdk.api.SkyscrapersApi;
import ru.flippy.skyscrapers.sdk.api.model.Pagination;
import ru.flippy.skyscrapers.sdk.api.model.User;
import ru.flippy.skyscrapers.sdk.listener.PaginationRequestListener;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        SkyscrapersApi.friends().get(1).execute(new PaginationRequestListener<List<User>>() {
            @Override
            public void onResponse(List<User> response, Pagination pagination) {

            }

            @Override
            public void onError(int errorCode) {

            }
        });
    }
}
