package ru.flippy.skyscrapers.application.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import ru.flippy.skyscrapers.R;
import ru.flippy.skyscrapers.api.SkyscrapersAPI;
import ru.flippy.skyscrapers.api.listener.SimpleRequestListener;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        SkyscrapersAPI.auth("Query", "zobega900").execute(new SimpleRequestListener() {
            @Override
            public void onSuccess() {
                SkyscrapersAPI.mail().send(3484192, "222").execute(new SimpleRequestListener() {
                    @Override
                    public void onSuccess() {
                        Snackbar.make(findViewById(android.R.id.content), "Success!", Snackbar.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(int errorCode) {

                    }
                });
            }

            @Override
            public void onError(int errorCode) {
                Snackbar.make(findViewById(android.R.id.content), "Error: " + errorCode, Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}
