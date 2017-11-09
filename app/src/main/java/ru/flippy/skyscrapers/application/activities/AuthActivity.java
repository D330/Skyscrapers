package ru.flippy.skyscrapers.application.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;

import ru.flippy.skyscrapers.R;
import ru.flippy.skyscrapers.sdk.api.SkyscrapersApi;
import ru.flippy.skyscrapers.sdk.api.model.Payment;
import ru.flippy.skyscrapers.sdk.api.model.SmsCountryGroup;
import ru.flippy.skyscrapers.sdk.api.model.SmsDonate;
import ru.flippy.skyscrapers.sdk.listener.ActionRequestListener;
import ru.flippy.skyscrapers.sdk.listener.PaymentSmsDonatesRequestListener;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        SkyscrapersApi.login("Query", "zobega900").execute(new ActionRequestListener() {
            @Override
            public void onSuccess() {
                SkyscrapersApi.payment().smsDonates(Payment.SMS).execute(new PaymentSmsDonatesRequestListener() {
                    @Override
                    public void onResponse(String sms, List<SmsCountryGroup> response) {
                        Log.d("Payment#smsDonates", "sms: " + sms);
                        for (SmsCountryGroup smsCountryGroup : response) {
                            Log.d("Payment#smsDonates", "country: " + smsCountryGroup.getCountry());
                            for (SmsDonate smsDonate : smsCountryGroup.getDonates()) {
                                Log.d("Payment#smsDonates", (smsDonate.isApproximately() ? "*" : "") + " " + smsDonate.getPrice() + " " + smsDonate.getCurrency() + " " + (smsDonate.withNds() ? "с ндс" : "") + "\n");
                            }
                            Log.d("Payment#smsDonates", "\n\n");
                        }
                    }

                    @Override
                    public void onError(int errorCode) {
                        Log.d("Payment#smsDonates", "errorCode: " + errorCode);
                    }
                });
            }

            @Override
            public void onError(int errorCode) {
                Log.d("Login#login", "errorCode: " + errorCode);
            }
        });
    }
}
