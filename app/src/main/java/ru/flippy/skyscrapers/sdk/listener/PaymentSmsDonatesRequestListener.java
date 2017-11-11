package ru.flippy.skyscrapers.sdk.listener;

import java.util.List;

import ru.flippy.skyscrapers.sdk.api.model.SmsCountryGroup;

public interface PaymentSmsDonatesRequestListener extends Errorable {
    void onResponse(String sms, List<SmsCountryGroup> response);
}