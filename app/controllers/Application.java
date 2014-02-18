package controllers;

import java.util.List;

import models.Board;
import models.Message;
import models.security.User;
import play.Logger;
import play.mvc.Before;
import play.mvc.Controller;
import utils.Globals;
import utils.SecurityUtils;

/**
 * @author royguo1988@gmail.com(Roy Guo)
 */
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
        Logger.info(request.headers.get("X-Real-IP") + " " + request.action);
        // 检查是否是匿名用户可访问的页面
        if (SecurityUtils.checkAnonymous(request.action, request.method)) return;
        User user = currentUser();
        if (SecurityUtils.checkPermission(user, request.action, request.method)) {
            return;
        }
        forbidden("没有此权限");
    }

    /**
     * 预先取出所有的版块.
     */
    @Before
    public static void findAllBoards() {
        List<Board> boards = Board.findAll();
        renderArgs.put("boards", boards);
    }
    
    @Before
    public static void hasUnOpenedMessage(){
        if(currentUser() == null) {
            return;
        }
        long count = Message.count("toUser.id = ? and opened = false", currentUser().id);
        renderArgs.put(Globals.HAS_UNOPENED_MESSAGE, count > 0);
    }

    /**
     * 默认的Action
     */
    public static void index() {
        render();
    }
}
