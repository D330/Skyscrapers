package ru.flippy.skyscrapers.sdk.api.methods;

import ru.flippy.skyscrapers.sdk.api.model.Donate;
import ru.flippy.skyscrapers.sdk.api.request.payment.PaymentChangePhoneRequest;
import ru.flippy.skyscrapers.sdk.api.request.payment.PaymentDonatesRequest;
import ru.flippy.skyscrapers.sdk.api.request.payment.PaymentMakeDonateRequest;
import ru.flippy.skyscrapers.sdk.api.request.payment.PaymentSmsDonatesRequest;
import ru.flippy.skyscrapers.sdk.api.request.payment.PaymentWebMoneyInvoice;

public class SkyscrapersPayment {

    public PaymentChangePhoneRequest changePhone(String phoneNumber) {
        return new PaymentChangePhoneRequest(phoneNumber);
    }

    public PaymentDonatesRequest donates(String type) {
        return new PaymentDonatesRequest(type);
    }

    public PaymentMakeDonateRequest makeDonate(String type, Donate donate) {
        return new PaymentMakeDonateRequest(type, donate);
    }

    public PaymentWebMoneyInvoice webmoneyInvoice(long wmid, String wallet, int dollars) {
        return new PaymentWebMoneyInvoice(wmid, wallet, dollars);
    }

    public PaymentSmsDonatesRequest smsDonates(String type) {
        return new PaymentSmsDonatesRequest(type);
    }
}
