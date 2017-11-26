package ru.flippy.skyscrapers.application.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;

import ru.flippy.skyscrapers.R;
import ru.flippy.skyscrapers.sdk.api.SkyscrapersApi;
import ru.flippy.skyscrapers.sdk.api.model.NoCityUser;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;
import ru.flippy.skyscrapers.sdk.listener.RequestListener;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        SkyscrapersApi.login("Query", "zobega900").execute(new ActionRequestListener() {
            @Override
            public void onSuccess() {
                SkyscrapersApi.support().ticketRate(1400517, true, false, true)
                        .execute(new ActionRequestListener() {
                            @Override
                            public void onSuccess() {
                                Log.d("Q", "success");
                            }

                            @Override
                            public void onError(int errorCode) {

                            }
                        });
            }

            @Override
            public void onError(int errorCode) {
                Log.d("Q", errorCode + "");
            }
        });
    }
}