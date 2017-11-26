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
    SourceCall loginPage();

    @FormUrlEncoded
    @POST("login/¿wicket:interface=:{wicket}:loginForm:loginForm::IFormSubmitListener::")
    SourceCall login(@Path("wicket") long wicket, @FieldMap HashMap<String, String> postData);

    @GET("tower/id/{userId}")
    SourceCall profile(@Path("userId") long userId);

    @GET("¿wicket:interface=:{wicket}:confirmLink::ILinkListener::")
    SourceCall confirm(@Path("wicket") long wicket);

    @GET("black/list")
    SourceCall blacklist();

    @FormUrlEncoded
    @POST("black/list/¿wicket:interface=:{wicket}:addForm::IFormSubmitListener::")
    SourceCall blacklistAdd(@Path("wicket") long wicket, @FieldMap HashMap<String, String> postData);

    @GET("black/list/¿wicket:interface=:{wicket}:paginator:container:last::ILinkListener::")
    SourceCall blacklistLastPage(@Path("wicket") long wicket);

    @GET("black/list/¿wicket:interface=:{wicket}:paginator:container:navigation:{page}:pageLink::ILinkListener::")
    SourceCall blacklistPagination(@Path("wicket") long wicket, @Path("page") int page);

    @GET("chat")
    SourceCall chat();

    @FormUrlEncoded
    @POST("chat/wicket:interface/:{wicket}:chatForm:messageForm::IFormSubmitListener::")
    SourceCall chatSend(@Path("wicket") long wicket, @FieldMap HashMap<String, String> postData);

    @GET("city")
    SourceCall city();

    @GET("tower/id/{userId}/¿wicket:interface=:{wicket}:guildInviteLink::ILinkListener::")
    SourceCall cityInvite(@Path("wicket") long wicket, @Path("userId") long userId);

    @GET("city/role/0/{userId}")
    SourceCall cityChangeRolePage(@Path("userId") long userId);

    @GET("city/role/0/{userId}/¿wicket:interface=:{wicket}:role{level}Link::ILinkListener::")
    SourceCall cityChangeRole(@Path("wicket") long wicket, @Path("userId") long userId, @Path("level") int level);

    @GET("city/about/0/{cityId}")
    SourceCall citySettings(@Path("cityId") long cityId);

    @FormUrlEncoded
    @POST("city/about/0/{cityId}/¿wicket:interface=:{wicket}:guildAboutForm::IFormSubmitListener::")
    SourceCall citySettingsChangeAbout(@Path("wicket") long wicket, @Path("cityId") long cityId, @FieldMap HashMap<String, String> postData);

    @FormUrlEncoded
    @POST("city/about/0/{cityId}/¿wicket:interface=:{wicket}:guildNameForm::IFormSubmitListener::")
    SourceCall citySettingsChangeName(@Path("wicket") long wicket, @Path("cityId") long cityId, @FieldMap HashMap<String, String> postData);

    @GET("forum/list")
    SourceCall forumSections();

    @GET("forum/0/{sectionId}/page/{page}")
    SourceCall forumTopics(@Path("sectionId") long sectionId, @Path("page") int page);

    @GET("forum/0/{sectionId}/¿wicket:interface=:{wicket}:markAsReadLink::ILinkListener::")
    SourceCall forumMarkAsRead(@Path("sectionId") long sectionId, @Path("wicket") long wicket);

    @GET("forum/topic/id/{topicId}/page/{page}")
    SourceCall forumTopic(@Path("topicId") long topicId, @Path("page") int page);

    @FormUrlEncoded
    @POST("forum/topic/id/{topicId}/page/{page}/¿wicket:bookmarkablePage=:ru.overmobile.towers.wicket.pages.StaticPostHandler")
    SourceCall forumComment(@Path("topicId") long topicId, @Path("page") int page, @FieldMap HashMap<String, String> postData);

    @GET("forum/topic/form/forumId/{sectionId}")
    SourceCall forumCreateTopicPage(@Path("sectionId") long sectionId);

    @FormUrlEncoded
    @POST("forum/topic/form/forumId/{sectionId}/¿wicket:bookmarkablePage=:ru.overmobile.towers.wicket.pages.StaticPostHandler")
    SourceCall forumCreateTopic(@Path("sectionId") long sectionId, @FieldMap HashMap<String, String> postData);

    @GET("forum/topic/form/id/{topicId}")
    SourceCall forumEditTopicPage(@Path("topicId") long topicId);

    @GET("forum/topic/form/id/{topicId}/¿wicket:bookmarkablePage=:ru.overmobile.towers.wicket.pages.StaticPostHandler")
    SourceCall forumEditTopic(@Path("topicId") long topicId, @FieldMap HashMap<String, String> postData);

    @GET("friends")
    SourceCall friends();

    @GET("friends/¿wicket:interface=:{wicket}:paginator:container:navigation:{page}:pageLink::ILinkListener::")
    SourceCall friendsPagination(@Path("wicket") long wicket, @Path("page") int page);

    @GET("tower/id/{userId}/¿wicket:interface=:{wicket}:addFriendLink::ILinkListener::")
    SourceCall friendsAdd(@Path("wicket") long wicket, @Path("userId") long userId);

    @FormUrlEncoded
    @POST("friends/¿wicket:interface=:{wicket}:addForm::IFormSubmitListener::")
    SourceCall friendsAdd(@Path("wicket") long wicket, @FieldMap HashMap<String, String> postData);

    @GET("tower/id/{userId}/¿wicket:interface=:{wicket}:removeFriendLink::ILinkListener::")
    SourceCall friendsRemove(@Path("wicket") long wicket, @Path("userId") long userId);

    @GET("mail")
    SourceCall mail();

    @GET("mail/page/{page}")
    SourceCall mailPagination(@Path("page") int page);

    @GET("mail/¿wicket:interface=:{wicket}:deleteLink::ILinkListener::")
    SourceCall mailDeleteRead(@Path("wicket") long wicket);

    @GET("mail/¿wicket:interface=:{wicket}:markAsReadLink::ILinkListener::")
    SourceCall mailMarkAsRead(@Path("wicket") long wicket);

    @GET("mail/send/id/{userId}")
    SourceCall mailSendPage(@Path("userId") long userId);

    @FormUrlEncoded
    @POST("mail/send/id/{userId}/wicket:interface/:{wicket}:sendMessageForm:messageForm::IFormSubmitListener::")
    SourceCall mailSend(@Path("wicket") long wicket, @Path("userId") long userId, @FieldMap HashMap<String, String> postData);

    @GET("mail/read/id/{dialogId}/page/{page}/¿wicket:interface=:{wicket}:historyLinkBlock:historyLink::ILinkListener::&action={action}")
    SourceCall mailUnlockDialogHistory(@Path("wicket") long wicket, @Path("dialogId") long dialogId, @Path("page") int page, @Path("action") long action);

    @GET("mail/read/id/{dialogId}/page/{page}")
    SourceCall mailDialogHistoryPagination(@Path("dialogId") long dialogId, @Path("page") int page);

    @GET("¿wicket:interface=:{wicket}:xMobilePayment:mobilePhonePanel:changePhoneBlock:changeLink::ILinkListener::")
    SourceCall paymentChangePhonePage(@Path("wicket") long wicket);

    @FormUrlEncoded
    @POST("¿wicket:interface=:{wicket}:xMobilePayment:mobilePhonePanel:emptyPhoneBlock:phoneForm::IFormSubmitListener::")
    SourceCall paymentChangePhone(@Path("wicket") long wicket, @FieldMap HashMap<String, String> postData);

    @GET("payment/type/{type}")
    SourceCall paymentDonatePage(@Path(encoded = true, value = "type") String type);

    @GET("¿wicket:interface=:{wicket}:{type}:paymentChoosePanel:link{donateId}::ILinkListener::")
    SourceCall paymentDonate(@Path("wicket") long wicket, @Path("type") String type, @Path("donateId") long donateId);

    @FormUrlEncoded
    @POST("payment/type/xWebmoneyInvoice/¿wicket:interface=:{wicket}:xWebmoneyInvoice:wmForm::IFormSubmitListener::")
    SourceCall paymentWebmoneyInvoice(@Path("wicket") long wicket, @FieldMap HashMap<String, String> postData);

    @GET("rating/type/{type}")
    SourceCall ratingUsers(@Path("type") int type);

    @GET("rating/type/{type}/¿wicket:interface=:{wicket}:paginator:container:navigation:{index}:pageLink::ILinkListener::")
    SourceCall ratingUsersPagination(@Path("type") int type, @Path("wicket") long wicket, @Path("index") int index);

    @GET("city/list")
    SourceCall ratingCity();

    @GET("city/list/¿wicket:interface=:{wicket}:paginator:container:navigation:{index}:pageLink::ILinkListener::")
    SourceCall ratingCityPagination(@Path("wicket") long wicket, @Path("index") int index);

    @GET("online/find/user")
    SourceCall searchUserPage();

    @FormUrlEncoded
    @POST("online/find/user/¿wicket:interface=:{wicket}:searchForm::IFormSubmitListener::")
    SourceCall searchUser(@Path("wicket") long wicket, @FieldMap HashMap<String, String> postData);

    @GET("online/find/user/¿wicket:interface=:{wicket}:searchResult:paginator:container:navigation:{index}:pageLink::ILinkListener::")
    SourceCall searchUserPagination(@Path("wicket") long wicket, @Path("index") int index);

    @GET("city/search")
    SourceCall searchCityPage();

    @FormUrlEncoded
    @POST("city/search/¿wicket:interface=:{wicket}:searchForm::IFormSubmitListener::")
    SourceCall searchCity(@Path("wicket") long wicket, @FieldMap HashMap<String, String> postData);

    @GET("city/search/¿wicket:interface=:{wicket}:searchResult:paginator:container:navigation:{index}:pageLink::ILinkListener::")
    SourceCall searchCityPagination(@Path("wicket") long wicket, @Path("index") int index);

    @GET("online/nocity")
    SourceCall searchNoCity();

    @GET("online")
    SourceCall searchOnline();

    @GET("online/¿wicket:interface=:{wicket}:paginator:container:navigation:{index}:pageLink::ILinkListener::")
    SourceCall searchOnlinePagination(@Path("wicket") long wicket, @Path("index") int index);

    @GET("settings")
    SourceCall settings();

    @FormUrlEncoded
    @POST("settings/¿wicket:interface=:{wicket}:aboutForm::IFormSubmitListener::")
    SourceCall settingsChangeAbout(@Path("wicket") long wicket, @FieldMap HashMap<String, String> postData);

    @FormUrlEncoded
    @POST("settings/¿wicket:interface=:{wicket}:birthdayForm::IFormSubmitListener::")
    SourceCall settingsChangeBirthday(@Path("wicket") long wicket, @FieldMap HashMap<String, String> postData);

    @GET("settings/¿wicket:interface=:{wicket}:guildSearchLink::ILinkListener::")
    SourceCall settingsChangeCityInvite(@Path("wicket") long wicket);

    @GET("changeLogin")
    SourceCall settingsChangeNickPage();

    @FormUrlEncoded
    @POST("changelogin/¿wicket:interface=:{wicket}:changeLoginForm::IFormSubmitListener::")
    SourceCall settingsChangeNick(@Path("wicket") long wicket, @FieldMap HashMap<String, String> postData);

    @FormUrlEncoded
    @POST("settings/¿wicket:interface=:{wicket}:passwordForm::IFormSubmitListener::")
    SourceCall settingsChangePassword(@Path("wicket") long wicket, @FieldMap HashMap<String, String> postData);

    @GET("settings/¿wicket:interface=:{wicket}:changeSex::ILinkListener::")
    SourceCall settingsChangeSex(@Path("wicket") long wicket);

    @GET("support/ticket/0/{ticketId}")
    SourceCall supportTicket(@Path("ticketId") long ticketId);

    @FormUrlEncoded
    @POST("support/ticket/0/{ticketId}/wicket:interface/:{wicket}:commentForm::IFormSubmitListener::")
    SourceCall supportTicketComment(@Path("ticketId") long ticketId, @Path("wicket") long wicket, @FieldMap HashMap<String, String> postData);

    @GET("support/rate/0/{ticketId}")
    SourceCall supportRateTicketPage(@Path("ticketId") long ticketId);

    @FormUrlEncoded
    @POST("support/rate/0/{ticketId}/wicket:interface/:{wicket}:rateForm::IFormSubmitListener::")
    SourceCall supportRateTicket(@Path("ticketId") long ticketId, @Path("wicket") long wicket, @FieldMap HashMap<String, String> postData);

    @GET("support")
    SourceCall supportTickets();

    @GET("support/¿wicket:interface=:{wicket}:paginator:container:navigation:{index}:pageLink::ILinkListener::")
    SourceCall supportTicketsPagination(@Path("wicket") long wicket, @Path("index") int index);

    @GET("support/create/type/cat_{type}")
    SourceCall supportCreateTicketPage(@Path("type") String type);

    @FormUrlEncoded
    @POST("support/create/type/cat_{type}/wicket:interface/:{wicket}:supportForm::IFormSubmitListener::")
    SourceCall supportCreateTicket(@Path("type") String type, @Path("wicket") long wicket, @FieldMap HashMap<String, String> postData);
}
