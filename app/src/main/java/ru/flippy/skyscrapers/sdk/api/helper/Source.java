package ru.flippy.skyscrapers.sdk.api.helper;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import ru.flippy.skyscrapers.sdk.api.model.Pagination;
import ru.flippy.skyscrapers.sdk.util.Utils;

public class Source extends Document {

    public Source(String html) {
        super(html);
    }

    public boolean checkPageError() {
        return has("div[class=m5 cntr amount]:contains(Произошла какая-то ошибка)");
    }

    public boolean checkAccessDenied() {
        return has("div[class=m5 cntr amount]:contains(Данная страница недоступна)");
    }

    public boolean has(String cssQuery) {
        return !select(cssQuery).isEmpty();
    }

    public boolean hasFeedBack(String type, String feedbackContains) {
        return has("span.feedbackPanel" + type + ":contains(" + feedbackContains + ")");
    }

    public boolean hasForm(String actionContains) {
        return has("form[action*=" + actionContains + "]");
    }

    public boolean hasLink(String hrefContains) {
        return getLink(hrefContains) != null;
    }

    public Element getLink(String hrefContains) {
        return select("a[href*=" + hrefContains + "]").first();
    }

    public Pagination pagination() {
        Pagination pagination = new Pagination();

        return pagination;
    }

    public int getPageCount(int type) {
        if (!has("div.pgn")) {
            return 1;
        } else {
            if (type == Pagination.Type.NORMAL) {
                return Integer.parseInt(select("div.pgn>pag").last().text());
            } else {
                Element lastPaginationElement = select("div.pgn").first().select(".pag").last();
                if (lastPaginationElement.tagName().equals("a")) {
                    return Utils.getValueAfterLastSlash(lastPaginationElement.attr("href")).intValue();
                } else {
                    return Integer.parseInt(lastPaginationElement.text());
                }
            }
        }
    }

    public long wicket() {
        Element wicketElement = select("form[action*=wicket], a[href*=wicket]").first();
        if (wicketElement != null) {
            String url = wicketElement.hasAttr("href") ? wicketElement.attr("href") : wicketElement.attr("action");
            String[] splitUrl = url.split(":");
            for (int i = 0; i < splitUrl.length; i++) {
                if (splitUrl[i].contains("interface")) {
                    return Long.parseLong(splitUrl[i + 1]);
                }
            }
        }
        return 0;
    }
}
