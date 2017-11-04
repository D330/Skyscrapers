package ru.flippy.skyscrapers.sdk.api.helper;

import org.jsoup.nodes.Element;

import ru.flippy.skyscrapers.sdk.util.Utils;

public class Parser {

    public static final String FEEDBACK_ERROR = "ERROR";
    public static final String FEEDBACK_INFO = "INFO";

    public static final int TYPE_WICKET = 0;
    public static final int TYPE_NORMAL = 1;

    private Element element;

    public static Parser from(Element element) {
        Parser parser = new Parser();
        parser.element = element;
        return parser;
    }

    public boolean checkFeedBack(String type, String feedbackContains) {
        return !element.select("span.feedbackPanel" + type + ":contains(" + feedbackContains + ")").isEmpty();
    }

    public Element getLink(String hrefContains) {
        return element.select("a[href*=" + hrefContains + "]").first();
    }

    public int getPageCount(int type) {
        if (element.select("div.pgn").isEmpty()) {
            return 1;
        } else {
            if (type == TYPE_WICKET) {
                return Integer.parseInt(element.select("div.pgn>pag").last().text());
            } else if (type == TYPE_NORMAL) {
                Element lastPaginatorElement = element.select("div.pgn").first().select(".pag").last();
                if (lastPaginatorElement.tagName().equals("a")) {
                    return Utils.getValueAfterLastSlash(lastPaginatorElement.attr("href")).intValue();
                } else {
                    return Integer.parseInt(lastPaginatorElement.text());
                }
            }
        }
        return 0;
    }

    public boolean checkPageError() {
        return !element.select("div[class=m5 cntr amount]:contains(Произошла какая-то ошибка), div[class=m5 cntr amount]:contains(Данная страница недоступна)").isEmpty();
    }

    public long getWicket() {
        Element wicketElement = element.select("form[action*=wicket], a[href*=wicket]").first();
        String url = wicketElement.hasAttr("href") ? wicketElement.attr("href") : wicketElement.attr("action");
        String[] splitUrl = url.split(":");
        for (int i = 0; i < splitUrl.length; i++) {
            if (splitUrl[i].contains("interface")) {
                return Long.parseLong(splitUrl[i + 1]);
            }
        }
        return 0;
    }
}
