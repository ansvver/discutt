package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import models.Reply;
import models.Topic;
import utils.JSONUtils;

public class Topics extends Application {

    public static void save(Topic topic) {
        topic.save();
        topic.user = currentUser();
        renderJSON(JSONUtils.toJSON(topic, "id", "title"));
    }

    public static void getJSON(long id) {
        Topic topic = Topic.findById(id);

        Map<String, Object> topicMap = JSONUtils.toMap(topic, "id", "title", "content");
        // 将当前主题下的回复仿佛JSON对象, 同时为每一个Reply添加User的JSON对象
        List<Object> replies = new ArrayList<Object>();
        for (Reply reply : topic.replies) {
            Map<String, Object> replyMap = JSONUtils.toMap(reply, "content");
            Map<String, Object> userMap = JSONUtils.toMap(reply.user, "nickName");
            replyMap.put("user", userMap);
            replies.add(replyMap);
        }
        topicMap.put("replies", replies);
        // 将当前主题的User对象放进去
        topicMap.put("user", JSONUtils.toMap(topic.user, "nickName"));
        renderJSON(JSONUtils.gson.toJson(topicMap));
    }

    public static void loadJSON(int page, int pageSize) {
        List<Topic> topics = Topic.findAll();
        renderJSON(JSONUtils.toJSONForList(topics, "id", "title"));
    }
}
