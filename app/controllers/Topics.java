package controllers;

import java.util.Date;
import java.util.List;

import models.Board;
import models.Topic;
import play.Logger;
import utils.DateUtil;
import utils.json.JSONObject;
import utils.json.JSONUtil;

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
        Logger.info(topic.date);
        JSONObject json =
                JSONUtil.toJSONForObject(topic, "id", "title", "content", "date", "replyCount");
        json.addSubObject("user", "nickName");
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
        }
        renderJSON(JSONUtil.toJSONForList(topics, "id", "title", "replyCount"));
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
