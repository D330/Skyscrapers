package ru.flippy.skyscrapers.sdk.api.request.city;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.flippy.skyscrapers.sdk.api.helper.Parser;
import ru.flippy.skyscrapers.sdk.api.model.Page;
import ru.flippy.skyscrapers.sdk.api.model.city.Role;
import ru.flippy.skyscrapers.sdk.api.request.BaseRequest;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.RequestListener;

public class CityRolesRequest extends BaseRequest {

    public static final int ACCESS_DENIED = 0;

    private long userId;

    public CityRolesRequest(long userId) {
        this.userId = userId;
    }

    public void execute(final RequestListener<List<Role>> listener) {
        RetrofitClient.getApi().cityChangeRolePage(userId).enqueue(new Callback<Page>() {
            @Override
            public void onResponse(Call<Page> call, Response<Page> response) {
                Page page = response.body();
                if (!response.isSuccessful() || page == null) {
                    listener.onError(UNKNOWN);
                } else {
                    Document document = page.getDocument();
                    if (Parser.from(document).checkPageError()) {
                        listener.onError(ACCESS_DENIED);
                    } else {
                        List<Role> roles = new ArrayList<>();
                        for (Element roleSeparator : document.select("div[class=m5]").first().select("div[class=hr]")) {
                            Element nameElement = roleSeparator.previousElementSibling();
                            Role role = new Role();
                            if (nameElement.tagName().equals("div")) {
                                StringBuilder description = new StringBuilder();
                                for (Element descriptionItem : nameElement.select("div.small")) {
                                    if (description.toString().length() > 0) {
                                        description.append("\n");
                                    }
                                    description.append(descriptionItem.text());
                                }
                                role.setDescription(description.toString());
                                nameElement = nameElement.previousElementSibling();
                            }
                            role.setName(nameElement.text());
                            if (nameElement.tagName().equals("a")) {
                                role.setAvailable(true);
                                role.setLevel(Integer.parseInt(nameElement.attr("href").split("role")[1].split("Link")[0]));
                            }
                            roles.add(role);
                        }
                        listener.onResponse(roles);
                    }
                }
            }

            @Override
            public void onFailure(Call<Page> call, Throwable t) {
                listener.onError(NETWORK);
            }
        });
    }
}
