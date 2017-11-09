package ru.flippy.skyscrapers.sdk.api.retrofit;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ru.flippy.skyscrapers.sdk.api.model.Page;

public interface ApiService {

    String ENDPOINT = "http://nebo.mobi/";

    @GET("login")
    Call<Page> loginPage();

    @FormUrlEncoded
    @POST("login/¿wicket:interface=:{wicket}:loginForm:loginForm::IFormSubmitListener::")
    Call<Page> login(@Path("wicket") long wicket, @FieldMap HashMap<String, String> postData);

    @GET("tower/id/{userId}")
    Call<Page> profile(@Path("userId") long userId);

    @GET("¿wicket:interface=:{wicket}:confirmLink::ILinkListener::")
    Call<Page> confirm(@Path("wicket") long wicket);

    @GET("black/list")
    Call<Page> blacklist();

    @FormUrlEncoded
    @POST("black/list/¿wicket:interface=:{wicket}:addForm::IFormSubmitListener::")
    Call<Page> blacklistAdd(@Path("wicket") long wicket, @FieldMap HashMap<String, String> postData);

    @GET("black/list/¿wicket:interface=:{wicket}:paginator:container:last::ILinkListener::")
    Call<Page> blacklistLastPage(@Path("wicket") long wicket);

    @GET("black/list/¿wicket:interface=:{wicket}:paginator:container:navigation:{page}:pageLink::ILinkListener::")
    Call<Page> blacklistPagination(@Path("wicket") long wicket, @Path("page") int page);

    @GET("chat")
    Call<Page> chat();

    @FormUrlEncoded
    @POST("chat/wicket:interface/:{wicket}:chatForm:messageForm::IFormSubmitListener::")
    Call<Page> chatSend(@Path("wicket") long wicket, @FieldMap HashMap<String, String> postData);

    @GET("city")
    Call<Page> city();

    @GET("tower/id/{userId}/¿wicket:interface=:{wicket}:guildInviteLink::ILinkListener::")
    Call<Page> cityInvite(@Path("wicket") long wicket, @Path("userId") long userId);

    @GET("city/role/0/{userId}")
    Call<Page> cityChangeRolePage(@Path("userId") long userId);

    @GET("city/role/0/{userId}/¿wicket:interface=:{wicket}:role{level}Link::ILinkListener::")
    Call<Page> cityChangeRole(@Path("wicket") long wicket, @Path("userId") long userId, @Path("level") int level);

    @GET("city/about/0/{cityId}")
    Call<Page> citySettings(@Path("cityId") long cityId);

    @FormUrlEncoded
    @POST("city/about/0/{cityId}/¿wicket:interface=:{wicket}:guildAboutForm::IFormSubmitListener::")
    Call<Page> citySettingsChangeAbout(@Path("wicket") long wicket, @Path("cityId") long cityId, @FieldMap HashMap<String, String> postData);

    @FormUrlEncoded
    @POST("city/about/0/{cityId}/¿wicket:interface=:{wicket}:guildNameForm::IFormSubmitListener::")
    Call<Page> citySettingsChangeName(@Path("wicket") long wicket, @Path("cityId") long cityId, @FieldMap HashMap<String, String> postData);

    @GET("friends")
    Call<Page> friends();

    @GET("friends/¿wicket:interface=:{wicket}:paginator:container:navigation:{page}:pageLink::ILinkListener::")
    Call<Page> friendsPagination(@Path("wicket") long wicket, @Path("page") int page);

    @GET("tower/id/{userId}/¿wicket:interface=:{wicket}:addFriendLink::ILinkListener::")
    Call<Page> friendsAdd(@Path("wicket") long wicket, @Path("userId") long userId);

    @FormUrlEncoded
    @POST("friends/¿wicket:interface=:{wicket}:addForm::IFormSubmitListener::")
    Call<Page> friendsAdd(@Path("wicket") long wicket, @FieldMap HashMap<String, String> postData);

    @GET("tower/id/{userId}/¿wicket:interface=:{wicket}:removeFriendLink::ILinkListener::")
    Call<Page> friendsRemove(@Path("wicket") long wicket, @Path("userId") long userId);

    @GET("mail")
    Call<Page> mail();

    @GET("mail/page/{page}")
    Call<Page> mailPagination(@Path("page") int page);

    @GET("mail/¿wicket:interface=:{wicket}:deleteLink::ILinkListener::")
    Call<Page> mailDeleteRead(@Path("wicket") long wicket);

    @GET("mail/¿wicket:interface=:{wicket}:markAsReadLink::ILinkListener::")
    Call<Page> mailMarkAsRead(@Path("wicket") long wicket);

    @GET("mail/send/id/{userId}")
    Call<Page> mailSendPage(@Path("userId") long userId);

    @FormUrlEncoded
    @POST("mail/send/id/{userId}/wicket:interface/:{wicket}:sendMessageForm:messageForm::IFormSubmitListener::")
    Call<Page> mailSend(@Path("wicket") long wicket, @Path("userId") long userId, @FieldMap HashMap<String, String> postData);

    @GET("mail/read/id/{dialogId}/page/{page}/¿wicket:interface=:{wicket}:historyLinkBlock:historyLink::ILinkListener::&action={action}")
    Call<Page> mailUnlockDialogHistory(@Path("wicket") long wicket, @Path("dialogId") long dialogId, @Path("page") int page, @Path("action") long action);

    @GET("mail/read/id/{dialogId}/page/{page}")
    Call<Page> mailDialogHistoryPagination(@Path("dialogId") long dialogId, @Path("page") int page);

    @GET("¿wicket:interface=:{wicket}:xMobilePayment:mobilePhonePanel:changePhoneBlock:changeLink::ILinkListener::")
    Call<Page> paymentChangePhonePage(@Path("wicket") long wicket);

    @FormUrlEncoded
    @POST("¿wicket:interface=:{wicket}:xMobilePayment:mobilePhonePanel:emptyPhoneBlock:phoneForm::IFormSubmitListener::")
    Call<Page> paymentChangePhone(@Path("wicket") long wicket, @FieldMap HashMap<String, String> postData);

    @GET("payment/type/{type}")
    Call<Page> paymentDonatePage(@Path(encoded = true, value = "type") String type);

    @GET("¿wicket:interface=:{wicket}:{type}:paymentChoosePanel:link{donateId}::ILinkListener::")
    Call<Page> paymentDonate(@Path("wicket") long wicket, @Path("type") String type, @Path("donateId") long donateId);

    @FormUrlEncoded
    @POST("payment/type/xWebmoneyInvoice/¿wicket:interface=:{wicket}:xWebmoneyInvoice:wmForm::IFormSubmitListener::")
    Call<Page> paymentWebmoneyInvoice(@Path("wicket") long wicket, @FieldMap HashMap<String, String> postData);

    @GET("settings")
    Call<Page> settings();

    @FormUrlEncoded
    @POST("settings/¿wicket:interface=:{wicket}:aboutForm::IFormSubmitListener::")
    Call<Page> settingsChangeAbout(@Path("wicket") long wicket, @FieldMap HashMap<String, String> postData);

    @FormUrlEncoded
    @POST("settings/¿wicket:interface=:{wicket}:birthdayForm::IFormSubmitListener::")
    Call<Page> settingsChangeBirthday(@Path("wicket") long wicket, @FieldMap HashMap<String, String> postData);

    @GET("settings/¿wicket:interface=:{wicket}:guildSearchLink::ILinkListener::")
    Call<Page> settingsChangeCityInvite(@Path("wicket") long wicket);

    @GET("changeLogin")
    Call<Page> settingsChangeNickPage();

    @FormUrlEncoded
    @POST("changelogin/¿wicket:interface=:{wicket}:changeLoginForm::IFormSubmitListener::")
    Call<Page> settingsChangeNick(@Path("wicket") long wicket, @FieldMap HashMap<String, String> postData);

    @FormUrlEncoded
    @POST("settings/¿wicket:interface=:{wicket}:passwordForm::IFormSubmitListener::")
    Call<Page> settingsChangePassword(@Path("wicket") long wicket, @FieldMap HashMap<String, String> postData);

    @GET("settings/¿wicket:interface=:{wicket}:changeSex::ILinkListener::")
    Call<Page> settingsChangeSex(@Path("wicket") long wicket);

}
