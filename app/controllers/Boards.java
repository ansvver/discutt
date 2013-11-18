package controllers;

import models.Board;

public class Boards extends Application {

    public static void index(long id) {
        Board board = Board.findById(id);
        render(board);
    }
}
