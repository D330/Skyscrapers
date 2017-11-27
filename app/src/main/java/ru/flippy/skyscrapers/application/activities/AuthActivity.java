package ru.flippy.skyscrapers.application.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;

import ru.flippy.skyscrapers.R;
import ru.flippy.skyscrapers.sdk.api.SkyscrapersApi;
import ru.flippy.skyscrapers.sdk.api.model.NoCityUser;
import ru.flippy.skyscrapers.sdk.api.model.profile.Cup;
import ru.flippy.skyscrapers.sdk.api.model.profile.CupStep;
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
                SkyscrapersApi.cup().cup().execute(new RequestListener<Cup>() {
                    @Override
                    public void onResponse(Cup response) {
                        for (CupStep step : response.getSteps()) {
                            Log.d("Q", step.getTitle() + " " + step.getDescription() + " " + step.isDone());
                        }
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