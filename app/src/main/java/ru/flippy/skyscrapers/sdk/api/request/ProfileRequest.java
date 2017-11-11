package ru.flippy.skyscrapers.sdk.api.request;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.flippy.skyscrapers.sdk.SkyscrapersSDK;
import ru.flippy.skyscrapers.sdk.api.model.Date;
import ru.flippy.skyscrapers.sdk.api.model.Profile;
import ru.flippy.skyscrapers.sdk.api.model.User;
import ru.flippy.skyscrapers.sdk.api.model.profile.Award;
import ru.flippy.skyscrapers.sdk.api.model.profile.Bonus;
import ru.flippy.skyscrapers.sdk.api.model.profile.Business;
import ru.flippy.skyscrapers.sdk.api.model.profile.Cars;
import ru.flippy.skyscrapers.sdk.api.model.profile.CityInfo;
import ru.flippy.skyscrapers.sdk.api.model.profile.Experience;
import ru.flippy.skyscrapers.sdk.api.model.profile.Marriage;
import ru.flippy.skyscrapers.sdk.api.model.profile.Personal;
import ru.flippy.skyscrapers.sdk.api.retrofit.DocumentCallback;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.RequestListener;
import ru.flippy.skyscrapers.sdk.util.Utils;

public class ProfileRequest {

    private long userId;

    public ProfileRequest(long userId) {
        this.userId = userId;
    }

    public void execute(final RequestListener<Profile> listener) {
        RetrofitClient.getApi().profile(userId).setErrorPoint(listener).enqueue(new DocumentCallback() {
            @Override
            public void onResponse(Document document, long wicket) {
                Profile profile = new Profile();
                boolean self = SkyscrapersSDK.getUserId() == userId;
                Element headerElement = document.select("div.snow:contains(«):contains(»):contains(Уровень:):has(span.user)").first().parent();
                profile.setId(userId);
                profile.setNick(headerElement.select("span.user").first().text());
                profile.setLevel(Integer.parseInt(headerElement.select("span.fll>strong.white").last().text()));
                profile.setFloorCount(Integer.parseInt(headerElement.select("span.flr>strong.white").last().text()));
                double floorStars = 0;
                for (Element starElement : headerElement.select("div>img[src*=star]:not(img[src*=gray])")) {
                    floorStars += starElement.attr("src").contains("half") ? 0.5 : 1;
                }
                profile.setFloorStars(floorStars);
                profile.setCupCount(document.select("div.cntr>a[href*=/cup]>span").size());
                List<Award> awards = new ArrayList<>();
                for (Element awardElement : document.select("div>div.nfl.cntr>div.inbl.txtal")) {
                    Element awardNameElement = awardElement.select("div>a.amount.tdn").first();
                    Elements awardBonusElements = awardElement.select("div.small").first().select("span.buff");
                    String name = awardElement.select("strong").text();
                    String url = awardElement.attr("href");
                    int marketing = Integer.parseInt(awardBonusElements.first().text().replaceAll("\\D", ""));
                    int relations = Integer.parseInt(awardBonusElements.last().text().replaceAll("\\D", ""));
                    int type = Award.UNKNOWN;
                    if (name.equals("Кубок чемпионов!")) {
                        String iconSrc = awardElement.select("div.fll").attr("src");
                        if (iconSrc.contains("99")) {
                            type = Award.GOLD_CUP;
                        } else if (iconSrc.contains("98")) {
                            type = Award.SILVER_CUP;
                        }
                    } else if (name.equals("Призер турнира!")) {
                        type = Award.TOUR_WINNER;
                    }
                    awards.add(new Award(name, url, marketing, relations, type));
                }
                profile.setAwards(awards);
                //Parse city data
                Element cityElement = headerElement.select("a[href*=/city/]").first();
                if (cityElement != null) {
                    Element roleElement = cityElement.nextElementSibling();
                    Element statusElement = headerElement.select("span.notify").first();
                    Element budgetElement = document.select("span.minor:contains(Внесено в бюджет:)").first();
                    CityInfo cityInfo = new CityInfo();
                    cityInfo.setId(Utils.getValueAfterLastSlash(cityElement.attr("href")));
                    cityInfo.setName(cityElement.text());
                    cityInfo.setCanChangeRole(!self && roleElement.tagName().equals("a"));
                    cityInfo.setGuildInvite(!self && !document.select("a[class*=bl link tdn][href*=guildInviteLink]").isEmpty());
                    cityInfo.setRole(roleElement.text());
                    if (statusElement != null) {
                        cityInfo.setStatus(statusElement.text());
                    }
                    cityInfo.setExperience(document.select("span.minor:contains(Опыт города:)").first().parent().select("span>last-child").text());
                    cityInfo.setDayCount(Integer.parseInt(document.select("span.minor:contains(Дней в городе:)").first().parent().select("span:last-child").text()));
                    if (budgetElement != null) {
                        Elements budgetAdditions = budgetElement.select("span.nwr");
                        cityInfo.setBudgetCoins(Integer.parseInt(budgetAdditions.first().text().replace("'", "")));
                        cityInfo.setBudgetDollars(Integer.parseInt(budgetAdditions.last().text().replace("'", "")));
                    }
                    cityInfo.setBirthday(!document.select("div[class*=nfl balls]>strong:contains(Отмечает день города!").isEmpty());
                    profile.setCityInfo(cityInfo);
                }
                if (self) {
                    profile.setFriend(false);
                    profile.setSex(document.select("div.cntr>span:has(img[src*=/user/]):has(span.user):contains(" + SkyscrapersSDK.getUserNick() + ")").last().select("img").first().attr("alt").equals("м") ? User.sex.MAN : User.sex.WOMAN);
                    profile.setOnline(User.online.ONLINE);
                } else {
                    profile.setFriend(document.select("a[class*=bl link tdn][href*=addFriendLink]").isEmpty());
                    Element onlineElement = document.select("span.minor:contains(Статус:)").first().nextElementSibling();
                    profile.setSex(onlineElement.select("img[src*=/user/]").first().attr("alt").equals("м") ? User.sex.MAN : User.sex.WOMAN);
                    if (onlineElement.text().equals("онлайн")) {
                        profile.setOnline(User.online.ONLINE);
                    } else if (onlineElement.select("span.minor").first().text().equals("*")) {
                        profile.setOnline(User.online.ONLINE_STAR);
                    } else {
                        profile.setOnline(User.online.OFFLINE);
                        Date lastEntrance = new Date();
                        String[] lastEntranceArray = document.select("span.minor:contains(Посл. вход:)").first().nextElementSibling().text().split(" ");
                        lastEntrance.setDay(Integer.parseInt(lastEntranceArray[0]));
                        lastEntrance.setMonth(Utils.getMonthIndex(lastEntranceArray[1]));
                        lastEntrance.setYear(Integer.parseInt(lastEntranceArray[2]));
                        profile.setLastEntrance(lastEntrance);
                    }
                }
                //Parse personal data
                Elements personalElements = document.select("div.m5:has(b[class*=bl cntr]):contains(Персонал)>div:has(span.amount)>span[class*=small flr amount]");
                int happy = Integer.parseInt(personalElements.first().text().replace(" ", "").split("/")[0]);
                int specialists = Integer.parseInt(personalElements.last().text().replace(" ", "").split("/")[0]);
                int population = Integer.parseInt(personalElements.last().text().replace(" ", "").split("/")[1]);
                profile.setPersonal(new Personal(happy, specialists, population));
                //Parse business data
                List<Business> businessList = new ArrayList<>();
                Elements businessElements = document.select("div.m5:has(b[class*=bl cntr]:contains(Бизнес))>a[href*=/business/]");
                for (Element businessElement : businessElements) {
                    Element bonusElement = businessElement.select("span.small.flr.buff").first();
                    long id = Utils.getValueAfterLastSlash(businessElement.attr("href"));
                    String name = bonusElement.previousSibling().toString();
                    int bonus = Integer.parseInt(bonusElement.select("span").first().text());
                    businessList.add(new Business(id, name, bonus));
                }
                profile.setBusiness(businessList);
                //Parse cars
                Element carsElement = document.select("div.cntr:has(div.m5:contains(Техника:)):has(a.m5[href*=/auto/])").first();
                if (carsElement != null) {
                    int count = Integer.parseInt(carsElement.select("div.m5>span.amount").first().text());
                    int stars = carsElement.select("div.m5>img[src*=/star.png]").size();
                    List<Integer> carIds = new ArrayList<>();
                    for (Element carElement : carsElement.select("a.m5[href*=/auto/]")) {
                        carIds.add(Utils.getValueAfterLastSlash(carElement.attr("href")).intValue());
                    }
                    Collections.sort(carIds);
                    profile.setCars(new Cars(count, stars, carIds));
                }
                Element giftsElement = document.select("a[class=m5 bl link tdn][href*=/gift/]:contains(Подарки:)>span[class=small flr buff]>span").first();
                if (giftsElement != null) {
                    profile.setGiftCount(Integer.parseInt(giftsElement.text()));
                }
                Bonus bonus = new Bonus();
                Element bonusElement = document.select("div[class=nfl cntr small ny]:has(span[class=amount nwr]):has(span.amount):contains(Текущая выручка):contains(Рекорд)").first();
                bonus.setCurrentRevenue(Integer.parseInt(bonusElement.select("span[class=amount nwr]>span").text().replace("'", "")));
                bonus.setRecordRevenue(Integer.parseInt(bonusElement.select("span[class=amount]>span").text().replace("'", "")));
                Element vendorElement = bonusElement.select("a.tdn[href*=/vendor]").first();
                if (vendorElement != null && vendorElement.hasText()) {
                    bonus.setMarketing(Integer.parseInt(vendorElement.select("span:has(img[alt=Mr])>span.buff").first().text().replace("+", "").replace("%", "")));
                    bonus.setRelations(Integer.parseInt(vendorElement.select("span:has(img[alt=Pr])>span.buff").first().text().replace("+", "").replace("%", "")));
                }
                Element managerElement = bonusElement.select("a.tdn[href*=/vendor/buff/0/8]>span.buff:contains(Менеджер)>span.nwr>span>span").first();
                if (managerElement != null) {
                    bonus.setManagerTime(managerElement.text());
                }
                profile.setBonus(bonus);
                Element experienceElement = document.select("span.minor:contains(Опыт:)").first();
                if (experienceElement != null) {
                    Element currentExperienceElement = experienceElement.nextElementSibling().nextElementSibling();
                    Element maxExperienceElement = currentExperienceElement.nextElementSibling();
                    Element percentExperienceElement = maxExperienceElement.nextElementSibling().select("span>span.minor>span").first();
                    String current = currentExperienceElement.text();
                    String max = maxExperienceElement.text();
                    int percent = Integer.parseInt(percentExperienceElement.text());
                    profile.setExperience(new Experience(current, max, percent));
                }
                profile.setGameDayCount(Integer.parseInt(document.select("span.minor:contains(Дней в игре:)").first().nextElementSibling().text()));
                Element aboutElement = document.select("span.minor:contains(О себе:)").first();
                if (aboutElement != null) {
                    profile.setAbout(aboutElement.nextElementSibling().text());
                }
                Element marriageElement = document.select("span[class=white tdn]:contains(В браке с:)").first();
                if (marriageElement != null) {
                    Marriage marriage = new Marriage();
                    marriageElement = marriageElement.nextElementSibling();
                    Element marriageImg = marriageElement.select("img[src*=/user/]").first();
                    marriage.setSex(marriageImg.attr("alt").equals("м") ? User.sex.MAN : User.sex.WOMAN);
                    for (String betweenTire : marriageImg.attr("src").split("\\.")[0].split("-")) {
                        if (betweenTire.endsWith("0")) {
                            marriage.setLevel(Integer.parseInt(betweenTire));
                        }
                    }
                    Element marriageUser = marriageElement.select("span.user>a").first();
                    marriage.setId(Utils.getValueAfterLastSlash(marriageUser.attr("href")));
                    marriage.setNick(marriageUser.text());
                    if (marriageElement.select("span.minor").last().text().equals("*")) {
                        marriage.setOnline(User.online.ONLINE_STAR);
                    } else if (marriageImg.attr("src").contains("off")) {
                        marriage.setOnline(User.online.OFFLINE);
                    } else {
                        marriage.setOnline(User.online.ONLINE);
                    }
                    marriage.setNewlyweds(!document.select("div[class=nfl ny]>div.hearts>strong>span:contains(Молодожены!)").isEmpty());
                    profile.setMarriage(marriage);
                }
                profile.setBirthday(!document.select("div[class=nfl ny balls]>div.m5>strong>span:contains(День рождения!)").isEmpty());
                listener.onResponse(profile);
            }
        });
    }
}