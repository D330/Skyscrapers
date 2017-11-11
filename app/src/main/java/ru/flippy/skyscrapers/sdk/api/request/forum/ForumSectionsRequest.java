package ru.flippy.skyscrapers.sdk.api.request.forum;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import ru.flippy.skyscrapers.sdk.api.model.ForumSectionItem;
import ru.flippy.skyscrapers.sdk.api.retrofit.DocumentCallback;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.RequestListener;
import ru.flippy.skyscrapers.sdk.util.Utils;

public class ForumSectionsRequest {

    public void execute(final RequestListener<List<ForumSectionItem>> listener) {
        RetrofitClient.getApi().forumSections().setErrorPoint(listener).enqueue(new DocumentCallback() {
            @Override
            public void onResponse(Document document, long wicket) {
                Elements sectionElements = document.select("ul.frm>li");
                List<ForumSectionItem> sections = new ArrayList<>(sectionElements.size());
                for (Element sectionElement : sectionElements) {
                    ForumSectionItem section = new ForumSectionItem();
                    section.setId(Utils.getValueAfterLastSlash(sectionElement.select("a").first().attr("href")));
                    section.setName(sectionElement.select("span>span").first().text());
                    section.setDescription(sectionElement.select("span.small").first().text());
                    section.setRead(!sectionElement.select("img[src*=read]").isEmpty());
                    sections.add(section);
                }
                listener.onResponse(sections);
            }
        });
    }
}
