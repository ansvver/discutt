package controllers;

import java.util.List;

import models.Board;
import models.security.User;
import play.Logger;
import play.mvc.Before;
import play.mvc.Controller;
import utils.Globals;
import utils.SecurityUtils;

public class Application extends Controller {
    /**
     * 从session中获得当前用户.
     */
    public static User currentUser() {
        String userId = session.get(Globals.USER_ID);
        if (userId != null) {
            User user = User.findById(Long.parseLong(userId));
            if (user == null) {
                session.clear();
            }
            return user;
        }
        return null;
    }

    /**
     * 权限控制
     */
    @Before
    public static void checkPermission() {
        Logger.info(request.action);
        // 检查是否是匿名用户可访问的页面
        if (SecurityUtils.checkAnonymous(request.action, request.method)) return;
        User user = currentUser();
        if (SecurityUtils.checkPermission(user, request.action, request.method)) {
            return;
        }
        forbidden("没有此权限");
    }

    @Before
    public static void findAllBoards() {
        List<Board> boards = Board.findAll();
        renderArgs.put("boards", boards);
    }

    /**
     * 默认的Action
     */
    public static void index() {
        render();
    }
}
