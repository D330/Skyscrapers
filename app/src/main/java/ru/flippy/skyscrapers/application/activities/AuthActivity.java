package ru.flippy.skyscrapers.application.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;

import java.util.List;

import ru.flippy.skyscrapers.R;
import ru.flippy.skyscrapers.sdk.api.SkyscrapersApi;
import ru.flippy.skyscrapers.sdk.api.model.Donate;
import ru.flippy.skyscrapers.sdk.api.model.Payment;
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
                SkyscrapersApi.payment().donates(Payment.CARD).execute(new RequestListener<Pair<String, List<Donate>>>() {
                    @Override
                    public void onResponse(Pair<String, List<Donate>> response) {
                        Donate donate = response.second.get(0);
                        SkyscrapersApi.payment().makeDonate(Payment.CARD, donate).execute(new ActionRequestListener() {
                            @Override
                            public void onSuccess() {
                                Log.d("Payment#makeDonate", "success");
                            }

                            @Override
                            public void onError(int errorCode) {
                                Log.d("Payment#makeDonate", "error: " + errorCode);
                            }
                        });
                    }

                    @Override
                    public void onError(int errorCode) {
                        Log.d("Payment#donates()", "error: " + errorCode);
                    }
                });
            }

            @Override
            public void onError(int errorCode) {
                Log.d("Login#login()", "error: " + errorCode);
            }
        });
    }
}
