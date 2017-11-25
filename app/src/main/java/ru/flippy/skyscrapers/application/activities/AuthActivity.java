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
                SkyscrapersApi.search().noCity().execute(new RequestListener<List<NoCityUser>>() {
                    @Override
                    public void onResponse(List<NoCityUser> response) {
                        for (NoCityUser user : response) {
                            Log.d("Q", user.isFreeForInvitation() + " " +
                                    user.getDaysInGame() + " " +
                                    user.getId() + " " +
                                    user.getNick() + " " +
                                    user.getLevel() + " " +
                                    user.getOnline() + " " +
                                    user.getSex() + "\n\n");
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