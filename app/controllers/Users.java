package controllers;

import models.security.User;
import models.security.UserGroup;
import play.Logger;
import play.data.validation.Valid;
import play.libs.Codec;
import utils.Globals;


public class Users extends Application {

    public static void register() {
        render();
    }

    public static void login() {
        render();
    }

    public static void logout() {
        logoutSession();
        Application.index();
    }

    public static void loginProcess(User user) {
        String error = "账号或密码错误，请重新输入！";
        if (user != null && user.password != null) {
            User loginUser =
                    User.find("email = ? and password = ?", user.email, Codec.hexMD5(user.password))
                            .first();
            if (loginUser != null) {
                loginSession(loginUser.id, loginUser.nickName);
                // 进入用户的个人首页
                Application.index();
            }
        }
        render("Users/login.html", error, user);
    }

    /**
     * 用户首页
     */
    public static void home(Long userId) {
        if (userId == null) index();
        User user = User.findById(userId);
        if (user == null) index();
        render(user);
    }

    /**
     * 检查Email是否已经存在
     * @param email
     */
    public static void checkEmail(String email) {
        long count = User.find("email = ?", email).fetch(1).size();
        if (count > 0) {
            renderJSON("{\"valid\":0}");
        } else {
            renderJSON("{\"valid\":1}");
        }
    }

    public static void save(@Valid User user) {
        if (validation.hasErrors()) {
            validation.keep();
            register();
        }
        // 新注册用户默认是普通用户组
        UserGroup group = UserGroup.find("name = ?", Globals.GROUP_NORMAL).first();
        user.userGroup = group;
        user.save();
        loginSession(user.id, user.nickName);
        Logger.info(user.toString() + " saved !");
        Application.index();
    }

    private static void loginSession(long userId, String nickName) {
        session.put(Globals.USER_ID, userId);
        session.put(Globals.USER_NICK_NAME, nickName);
    }

    private static void logoutSession() {
        session.clear();
    }
}
