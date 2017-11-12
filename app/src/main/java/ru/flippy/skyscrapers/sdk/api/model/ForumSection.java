package ru.flippy.skyscrapers.sdk.api.model;

import java.util.List;

public class ForumSection {

    private List<TopicItem> topics;
    private List<User> moderators;

    public List<TopicItem> getTopics() {
        return topics;
    }

    public void setTopics(List<TopicItem> topics) {
        this.topics = topics;
    }

    public List<User> getModerators() {
        return moderators;
    }

    public void setModerators(List<User> moderators) {
        this.moderators = moderators;
    }
}
