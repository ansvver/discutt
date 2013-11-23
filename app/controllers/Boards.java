package controllers;

import models.Board;
import models.Topic;

public class Boards extends Application {

    public static void index(long id) {
        Board board = Board.findById(id);
        long topicCount = Topic.count();
        render(board, topicCount);
    }
}
