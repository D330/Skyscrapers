package ru.flippy.skyscrapers.sdk.api.request.forum;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.flippy.skyscrapers.sdk.api.model.ForumSectionItem;
import ru.flippy.skyscrapers.sdk.api.model.Page;
import ru.flippy.skyscrapers.sdk.api.request.BaseRequest;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.RequestListener;
import ru.flippy.skyscrapers.sdk.util.Utils;

public class ForumSectionsRequest extends BaseRequest {

    public void execute(final RequestListener<List<ForumSectionItem>> listener) {
        RetrofitClient.getApi().forumSections().enqueue(new Callback<Page>() {
            @Override
            public void onResponse(Call<Page> call, Response<Page> response) {
                Page page = response.body();
                if (!response.isSuccessful() || page == null) {
                    listener.onError(UNKNOWN);
                } else {
                    Elements sectionElements = page.getDocument().select("ul.frm>li");
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
            }

            @Override
            public void onFailure(Call<Page> call, Throwable t) {
                listener.onError(NETWORK);
            }
        });
    }
}
