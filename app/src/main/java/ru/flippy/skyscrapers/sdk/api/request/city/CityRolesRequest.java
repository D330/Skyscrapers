package ru.flippy.skyscrapers.sdk.api.request.city;

import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

import ru.flippy.skyscrapers.sdk.api.helper.Source;
import ru.flippy.skyscrapers.sdk.api.model.city.Role;
import ru.flippy.skyscrapers.sdk.api.retrofit.RetrofitClient;
import ru.flippy.skyscrapers.sdk.listener.RequestListener;
import ru.flippy.skyscrapers.sdk.listener.SourceCallback;

public class CityRolesRequest {

    private long userId;

    public CityRolesRequest(long userId) {
        this.userId = userId;
    }

    public void execute(final RequestListener<List<Role>> listener) {
        RetrofitClient.getApi().cityChangeRolePage(userId)
                .error(listener)
                .success(new SourceCallback() {
                    @Override
                    public void onResponse(Source doc) {
                        List<Role> roles = new ArrayList<>();
                        for (Element roleSeparator : doc.select("div[class=m5]").first().select("div[class=hr]")) {
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
                });
    }
}
