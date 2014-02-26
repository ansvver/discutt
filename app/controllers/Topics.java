package controllers;

import java.util.Date;
import java.util.List;

import models.Board;
import models.ReadRecord;
import models.Topic;
import models.security.User;
import utils.DateUtil;
import utils.json.JSONObject;
import utils.json.JSONUtil;


/**
 * @author royguo1988@gmail.com(Roy Guo)
 */
public class Topics extends Application {

    public static void save(Topic topic, long boardID) {
        topic.user = currentUser();
        topic.lastUpdate = topic.date = DateUtil.format(new Date());
        topic.board = Board.findById(boardID);
        topic.save();
        renderJSON(JSONUtil.toJSONForObject(topic, "id", "title", "date", "replyCount").toString());
    }

    /**
     * 获得指定的Topic
     * @param id
     */
    public static void getJSON(long id) {
        Topic topic = Topic.findById(id);
        // 更新阅读记录
        User user = currentUser();
        if(user != null){
            ReadRecord record = ReadRecord.find("user.id = ? and topic.id = ?", user.id, topic.id).first();
            if(record == null) {
                record = new ReadRecord(user, topic);
            }
            record.lastReplySize = topic.replies.size();
            record.save();
        }
        
        JSONObject json =
                JSONUtil.toJSONForObject(topic, "id", "title", "content", "date", "replyCount");
        json.addSubObject("user", "nickName", "id");
        renderJSON(json.toString());
    }

    /**
     * 获得指定分页的一组Topic
     * @param boardID
     * @param page
     * @param pageSize
     */
    public static void loadJSON(long boardID, int page, int pageSize) {
        List<Topic> topics = null;
        if (page == -1) {
            int lastPage = (int) Topic.count() / pageSize;
            if (Topic.count() % pageSize > 0) lastPage++;
            topics =
                    Topic.find("board.id = ? order by lastUpdate desc", boardID).fetch(lastPage,
                            pageSize);
        } else {
            topics =
                    Topic.find("board.id = ? order by lastUpdate desc", boardID).fetch(page,
                            pageSize);
        }

        for (Topic topic : topics) {
            topic.replyCount = topic.replies.size();
            User user = currentUser();
            if(user != null){
                ReadRecord record = ReadRecord.find("user.id = ? and topic.id = ?",user.id, topic.id).first();
                if(record != null) {
                    if(record.lastReplySize < topic.replies.size()) {
                        topic.opened = false;
                    }
                }
            }
        }
        renderJSON(JSONUtil.toJSONForList(topics, "id", "opened", "title", "replyCount"));
    }

    /**
     * 获得主题总数
     * @param boardID
     */
    public static void countAll(long boardID) {
        long count = Topic.count("board.id = ?", boardID);
        renderJSON(count);
    }

    public static void delete(long id) {
        Topic topic = Topic.findById(id);
        topic.delete();
    }
}
