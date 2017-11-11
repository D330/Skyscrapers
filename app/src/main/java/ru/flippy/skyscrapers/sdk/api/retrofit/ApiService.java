package ru.flippy.skyscrapers.sdk.api.retrofit;

import java.util.HashMap;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    String ENDPOINT = "http://nebo.mobi/";

    @GET("login")
    AdvancedCall loginPage();

    @FormUrlEncoded
    @POST("login/¿wicket:interface=:{wicket}:loginForm:loginForm::IFormSubmitListener::")
    AdvancedCall login(@Path("wicket") long wicket, @FieldMap HashMap<String, String> postData);

    @GET("tower/id/{userId}")
    AdvancedCall profile(@Path("userId") long userId);

    @GET("¿wicket:interface=:{wicket}:confirmLink::ILinkListener::")
    AdvancedCall confirm(@Path("wicket") long wicket);

    @GET("black/list")
    AdvancedCall blacklist();

    @FormUrlEncoded
    @POST("black/list/¿wicket:interface=:{wicket}:addForm::IFormSubmitListener::")
    AdvancedCall blacklistAdd(@Path("wicket") long wicket, @FieldMap HashMap<String, String> postData);

    @GET("black/list/¿wicket:interface=:{wicket}:paginator:container:last::ILinkListener::")
    AdvancedCall blacklistLastPage(@Path("wicket") long wicket);

    @GET("black/list/¿wicket:interface=:{wicket}:paginator:container:navigation:{page}:pageLink::ILinkListener::")
    AdvancedCall blacklistPagination(@Path("wicket") long wicket, @Path("page") int page);

    @GET("chat")
    AdvancedCall chat();

    @FormUrlEncoded
    @POST("chat/wicket:interface/:{wicket}:chatForm:messageForm::IFormSubmitListener::")
    AdvancedCall chatSend(@Path("wicket") long wicket, @FieldMap HashMap<String, String> postData);

    @GET("city")
    AdvancedCall city();

    @GET("tower/id/{userId}/¿wicket:interface=:{wicket}:guildInviteLink::ILinkListener::")
    AdvancedCall cityInvite(@Path("wicket") long wicket, @Path("userId") long userId);

    @GET("city/role/0/{userId}")
    AdvancedCall cityChangeRolePage(@Path("userId") long userId);

    @GET("city/role/0/{userId}/¿wicket:interface=:{wicket}:role{level}Link::ILinkListener::")
    AdvancedCall cityChangeRole(@Path("wicket") long wicket, @Path("userId") long userId, @Path("level") int level);

    @GET("city/about/0/{cityId}")
    AdvancedCall citySettings(@Path("cityId") long cityId);

    @FormUrlEncoded
    @POST("city/about/0/{cityId}/¿wicket:interface=:{wicket}:guildAboutForm::IFormSubmitListener::")
    AdvancedCall citySettingsChangeAbout(@Path("wicket") long wicket, @Path("cityId") long cityId, @FieldMap HashMap<String, String> postData);

    @FormUrlEncoded
    @POST("city/about/0/{cityId}/¿wicket:interface=:{wicket}:guildNameForm::IFormSubmitListener::")
    AdvancedCall citySettingsChangeName(@Path("wicket") long wicket, @Path("cityId") long cityId, @FieldMap HashMap<String, String> postData);

    @GET("forum/list")
    AdvancedCall forumSections();

    @GET("forum/0/{sectionId}/page/{page}")
    AdvancedCall forumTopics(@Path("sectionId") long sectionId, @Path("page") int page);

    @GET("forum/0/{sectionId}/¿wicket:interface=:{wicket}:markAsReadLink::ILinkListener::")
    AdvancedCall forumMarkAsRead(@Path("sectionId") long sectionId, @Path("wicket") long wicket);

    @GET("forum/topic/id/{topicId}/page/{page}")
    AdvancedCall forumTopic(@Path("topicId") long topicId, @Path("page") int page);

    @FormUrlEncoded
    @POST("forum/topic/id/{topicId}/page/{page}/¿wicket:bookmarkablePage=:ru.overmobile.towers.wicket.pages.StaticPostHandler")
    AdvancedCall forumComment(@Path("topicId") long topicId, @Path("page") int page, @FieldMap HashMap<String, String> postData);

    @GET("forum/topic/form/forumId/{sectionId}")
    AdvancedCall forumCreateTopicPage(@Path("sectionId") long sectionId);

    @FormUrlEncoded
    @POST("forum/topic/form/forumId/{sectionId}/¿wicket:bookmarkablePage=:ru.overmobile.towers.wicket.pages.StaticPostHandler")
    AdvancedCall forumCreateTopic(@Path("sectionId") long sectionId, @FieldMap HashMap<String, String> postData);

    @GET("forum/topic/form/id/{topicId}")
    AdvancedCall forumEditTopicPage(@Path("topicId") long topicId);

    @GET("forum/topic/form/id/{topicId}/¿wicket:bookmarkablePage=:ru.overmobile.towers.wicket.pages.StaticPostHandler")
    AdvancedCall forumEditTopic(@Path("topicId") long topicId, @FieldMap HashMap<String, String> postData);

    @GET("friends")
    AdvancedCall friends();

    @GET("friends/¿wicket:interface=:{wicket}:paginator:container:navigation:{page}:pageLink::ILinkListener::")
    AdvancedCall friendsPagination(@Path("wicket") long wicket, @Path("page") int page);

    @GET("tower/id/{userId}/¿wicket:interface=:{wicket}:addFriendLink::ILinkListener::")
    AdvancedCall friendsAdd(@Path("wicket") long wicket, @Path("userId") long userId);

    @FormUrlEncoded
    @POST("friends/¿wicket:interface=:{wicket}:addForm::IFormSubmitListener::")
    AdvancedCall friendsAdd(@Path("wicket") long wicket, @FieldMap HashMap<String, String> postData);

    @GET("tower/id/{userId}/¿wicket:interface=:{wicket}:removeFriendLink::ILinkListener::")
    AdvancedCall friendsRemove(@Path("wicket") long wicket, @Path("userId") long userId);

    @GET("mail")
    AdvancedCall mail();

    @GET("mail/page/{page}")
    AdvancedCall mailPagination(@Path("page") int page);

    @GET("mail/¿wicket:interface=:{wicket}:deleteLink::ILinkListener::")
    AdvancedCall mailDeleteRead(@Path("wicket") long wicket);

    @GET("mail/¿wicket:interface=:{wicket}:markAsReadLink::ILinkListener::")
    AdvancedCall mailMarkAsRead(@Path("wicket") long wicket);

    @GET("mail/send/id/{userId}")
    AdvancedCall mailSendPage(@Path("userId") long userId);

    @FormUrlEncoded
    @POST("mail/send/id/{userId}/wicket:interface/:{wicket}:sendMessageForm:messageForm::IFormSubmitListener::")
    AdvancedCall mailSend(@Path("wicket") long wicket, @Path("userId") long userId, @FieldMap HashMap<String, String> postData);

    @GET("mail/read/id/{dialogId}/page/{page}/¿wicket:interface=:{wicket}:historyLinkBlock:historyLink::ILinkListener::&action={action}")
    AdvancedCall mailUnlockDialogHistory(@Path("wicket") long wicket, @Path("dialogId") long dialogId, @Path("page") int page, @Path("action") long action);

    @GET("mail/read/id/{dialogId}/page/{page}")
    AdvancedCall mailDialogHistoryPagination(@Path("dialogId") long dialogId, @Path("page") int page);

    @GET("¿wicket:interface=:{wicket}:xMobilePayment:mobilePhonePanel:changePhoneBlock:changeLink::ILinkListener::")
    AdvancedCall paymentChangePhonePage(@Path("wicket") long wicket);

    @FormUrlEncoded
    @POST("¿wicket:interface=:{wicket}:xMobilePayment:mobilePhonePanel:emptyPhoneBlock:phoneForm::IFormSubmitListener::")
    AdvancedCall paymentChangePhone(@Path("wicket") long wicket, @FieldMap HashMap<String, String> postData);

    @GET("payment/type/{type}")
    AdvancedCall paymentDonatePage(@Path(encoded = true, value = "type") String type);

    @GET("¿wicket:interface=:{wicket}:{type}:paymentChoosePanel:link{donateId}::ILinkListener::")
    AdvancedCall paymentDonate(@Path("wicket") long wicket, @Path("type") String type, @Path("donateId") long donateId);

    @FormUrlEncoded
    @POST("payment/type/xWebmoneyInvoice/¿wicket:interface=:{wicket}:xWebmoneyInvoice:wmForm::IFormSubmitListener::")
    AdvancedCall paymentWebmoneyInvoice(@Path("wicket") long wicket, @FieldMap HashMap<String, String> postData);

    @GET("settings")
    AdvancedCall settings();

    @FormUrlEncoded
    @POST("settings/¿wicket:interface=:{wicket}:aboutForm::IFormSubmitListener::")
    AdvancedCall settingsChangeAbout(@Path("wicket") long wicket, @FieldMap HashMap<String, String> postData);

    @FormUrlEncoded
    @POST("settings/¿wicket:interface=:{wicket}:birthdayForm::IFormSubmitListener::")
    AdvancedCall settingsChangeBirthday(@Path("wicket") long wicket, @FieldMap HashMap<String, String> postData);

    @GET("settings/¿wicket:interface=:{wicket}:guildSearchLink::ILinkListener::")
    AdvancedCall settingsChangeCityInvite(@Path("wicket") long wicket);

    @GET("changeLogin")
    AdvancedCall settingsChangeNickPage();

    @FormUrlEncoded
    @POST("changelogin/¿wicket:interface=:{wicket}:changeLoginForm::IFormSubmitListener::")
    AdvancedCall settingsChangeNick(@Path("wicket") long wicket, @FieldMap HashMap<String, String> postData);

    @FormUrlEncoded
    @POST("settings/¿wicket:interface=:{wicket}:passwordForm::IFormSubmitListener::")
    AdvancedCall settingsChangePassword(@Path("wicket") long wicket, @FieldMap HashMap<String, String> postData);

    @GET("settings/¿wicket:interface=:{wicket}:changeSex::ILinkListener::")
    AdvancedCall settingsChangeSex(@Path("wicket") long wicket);

}
