package controllers;

import java.util.List;
import java.util.Map;

import models.Reply;
import models.Topic;
import utils.JSONUtils;

public class Replies extends Application {

    public static void save(long topicID, Reply reply) {
        reply.topic = Topic.findById(topicID);
        reply.user = currentUser();
        reply.save();
        Map<String, Object> replyMap = JSONUtils.toMap(reply, "id", "content");
        replyMap.put("user", JSONUtils.toMap(reply.user, "nickName"));
        renderJSON(JSONUtils.gson.toJson(replyMap));
    }

    public static void loadJSON(long topicID) {
        Topic topic = Topic.findById(topicID);
        List<Object> replies = JSONUtils.toJSONListForList(topic.replies, "id", "content");
        renderJSON(JSONUtils.gson.toJson(replies));
    }
}
