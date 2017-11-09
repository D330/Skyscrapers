package ru.flippy.skyscrapers.sdk.listener;

import java.util.List;

import ru.flippy.skyscrapers.sdk.api.model.SmsCountryGroup;

public interface PaymentSmsDonatesRequestListener {
    void onResponse(String sms, List<SmsCountryGroup> response);

    void onError(int errorCode);
}