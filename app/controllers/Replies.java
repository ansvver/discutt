package controllers;

import java.util.Date;
import java.util.List;

import models.Reply;
import models.Topic;
import utils.DateUtil;
import utils.json.JSONObject;
import utils.json.JSONUtil;

public class Replies extends Application {

    public static void save(long topicID, Reply reply) {
        reply.topic = Topic.findById(topicID);
        reply.user = currentUser();
        reply.date = DateUtil.format(new Date());
        reply.save();

        reply.topic.lastUpdate = reply.date;
        reply.topic.save();

        JSONObject json = JSONUtil.toJSONForObject(reply, "id", "content", "date");
        json.addSubObject("user", "nickName");

        renderJSON(json.toString());
    }

    public static void loadJSON(long topicID, int page, int pageSize) {
        List<Reply> replies = null;
        if (page == -1) {
            int lastPage = (int) Reply.count("topic.id = ? ", topicID) / pageSize;
            if (Reply.count("topic.id = ?", topicID) % pageSize > 0) lastPage++;
            replies =
                    Reply.find("topic.id = ? order by id desc", topicID).fetch(lastPage, pageSize);
        } else {
            replies = Reply.find("topic.id = ? order by id desc", topicID).fetch(page, pageSize);
        }
        String json =
                JSONUtil.toJSONForListWithSubObject(replies,
                        new String[] {"id", "content", "date"}, "user", new String[] {"nickName"});
        renderJSON(json);
    }

    public static void countAll(long topicID) {
        int count = (int) Reply.count("topic.id = ?", topicID);
        renderJSON(count);
    }

    public static void delete(long id) {
        Reply reply = Reply.findById(id);
        reply.delete();
    }
}
