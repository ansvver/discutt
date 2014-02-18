package controllers;

import java.util.Date;
import java.util.List;

import models.ReadRecord;
import models.Reply;
import models.Topic;
import models.security.User;
import utils.DateUtil;
import utils.json.JSONObject;
import utils.json.JSONUtil;

/**
 * @author royguo1988@gmail.com(Roy Guo)
 */
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
            int lastPage = (int) Reply.count("topic.id = ? and deleted = false", topicID) / pageSize;
            if (Reply.count("topic.id = ? and deleted = false", topicID) % pageSize > 0) lastPage++;
            replies =
                    Reply.find("topic.id = ? and deleted = false order by id desc", topicID).fetch(lastPage, pageSize);
        } else {
            replies = Reply.find("topic.id = ? and deleted = false order by id desc", topicID).fetch(page, pageSize);
        }
        
        // 设置楼层
        int floor = replies.size();
        for (Reply reply : replies) {
            reply.floor = floor--;
        }
        String json =
                JSONUtil.toJSONForListWithSubObject(replies,
                        new String[] {"id", "floor", "content", "date"}, 
                            "user", new String[] {"nickName","id"});
        // 更新当前用户的阅读记录
        User user = currentUser();
        if(user != null){
            ReadRecord record = ReadRecord.find("user.id = ? and topic.id = ?", user.id, topicID).first();
            if(record == null) {
                Topic topic = Topic.findById(topicID);
                record = new ReadRecord(user, topic);
            }
            record.lastReplySize = replies.size();
            record.save();
        }
        renderJSON(json);
    }

    public static void countAll(long topicID) {
        int count = (int) Reply.count("topic.id = ?", topicID);
        renderJSON(count);
    }

    public static void delete(long id) {
        Reply reply = Reply.findById(id);
        reply.deleted = true;
        reply.save();
    }
}
