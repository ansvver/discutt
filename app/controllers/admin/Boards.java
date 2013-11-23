package controllers.admin;

import java.util.List;

import models.Board;
import controllers.Application;

/**
 * @author royguo1988@gmail.com(Roy Guo)
 */
public class Boards extends Application {

    public static void list() {
        List<Board> boards = Board.findAll();
        render(boards);
    }

    public static void edit(long id) {
        Board board = Board.findById(id);
        render(board);
    }

    public static void save(Board board) {
        board.save();
        list();
    }

    public static void delete(long id) {
        Board board = Board.findById(id);
        board.delete();
        list();
    }
}
