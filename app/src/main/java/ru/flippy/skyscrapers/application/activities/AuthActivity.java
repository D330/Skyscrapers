package ru.flippy.skyscrapers.application.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import ru.flippy.skyscrapers.R;
import ru.flippy.skyscrapers.sdk.api.SkyscrapersApi;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        SkyscrapersApi.login("Query", "zobega900").execute(new ActionRequestListener() {
            @Override
            public void onSuccess() {
                SkyscrapersApi.city().changeRole(17751268, 50).execute(new ActionRequestListener() {
                    @Override
                    public void onSuccess() {
                        Log.d("ChangeRole", "SUCCESS!");
                    }

                    @Override
                    public void onError(int errorCode) {
                        Log.d("ChangeRole", "error: " + errorCode);
                    }
                });
            }

            @Override
            public void onError(int errorCode) {
                Log.d("Auth", "error: " + errorCode);
            }
        });
    }
}
