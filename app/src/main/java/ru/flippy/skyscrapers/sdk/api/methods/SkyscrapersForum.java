package ru.flippy.skyscrapers.sdk.api.methods;

import ru.flippy.skyscrapers.sdk.api.request.forum.ForumCommentRequest;
import ru.flippy.skyscrapers.sdk.api.request.forum.ForumSectionsRequest;
import ru.flippy.skyscrapers.sdk.api.request.forum.ForumTopicRequest;
import ru.flippy.skyscrapers.sdk.api.request.forum.ForumTopicsRequest;

public class SkyscrapersForum {

    public ForumSectionsRequest sections() {
        return new ForumSectionsRequest();
    }

    public ForumTopicsRequest topics(long sectionId, int page) {
        return new ForumTopicsRequest(sectionId, page);
    }

    public ForumTopicRequest topic(long topicId, int page) {
        return new ForumTopicRequest(topicId, page);
    }

    public ForumCommentRequest comment(long topicId, int page, String message) {
        return new ForumCommentRequest(topicId, page, message);
    }

    public ForumCommentRequest reply(long topicId, long userId, int page, String message) {
        return new ForumCommentRequest(topicId, page, userId, message);
    }
}