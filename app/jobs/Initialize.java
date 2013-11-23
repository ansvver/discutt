package jobs;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import models.Board;
import models.security.Resource;
import models.security.User;
import models.security.UserGroup;
import play.Logger;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import utils.Globals;
import controllers.Application;
import controllers.admin.Admin;
import controllers.admin.Resources;
import controllers.admin.Users;

@OnApplicationStart
public class Initialize extends Job<String> {

    @Override
    public void doJob() throws Exception {
        initResources();
        initUsers();
        initTempData();
    }

    private void initUsers() {
        if (User.count() > 0) return;
        Logger.info("Default Users Initializing ... ");
        // 初始化超级管理员，超级管理员可以访问所有资源
        UserGroup admin = new UserGroup();
        admin.name = Globals.GROUP_ADMIN;
        admin.save();
        User root = new User("root@it.com", "root");
        root.nickName = "Root";
        root.userGroup = admin;
        root.save();

        // 初始化匿名用户组
        UserGroup anonymous = new UserGroup();
        anonymous.name = Globals.GROUP_ANONYMOUS;
        anonymous.save();
        initAnonymous(anonymous);

        // 初始化普通用户组，新注册用户默认都是该用户组
        UserGroup normalUser = new UserGroup();
        normalUser.name = Globals.GROUP_NORMAL;
        normalUser.save();
        initNormalUser(normalUser);

        User u = new User("admin@admin.com", "admin");
        u.nickName = "admin";
        u.userGroup = normalUser;
        u.save();
    }

    /**
     * 初始化匿名用户的权限
     */
    private void initAnonymous(UserGroup anonymous) {
        anonymous.resources.add(Resource.createOrFetch("Application.index"));
        anonymous.resources.add(Resource.createOrFetch("Users.register"));
        anonymous.resources.add(Resource.createOrFetch("Users.save"));
        anonymous.resources.add(Resource.createOrFetch("Users.checkEmail"));
        anonymous.resources.add(Resource.createOrFetch("Users.login"));
        anonymous.resources.add(Resource.createOrFetch("Users.logout"));
        anonymous.resources.add(Resource.createOrFetch("Users.loginProcess"));

        anonymous.resources.add(Resource.createOrFetch("Boards.index"));

        anonymous.resources.add(Resource.createOrFetch("Topics.getJSON"));
        anonymous.resources.add(Resource.createOrFetch("Topics.loadJSON"));
        anonymous.resources.add(Resource.createOrFetch("Topics.countAll"));

        anonymous.resources.add(Resource.createOrFetch("Replies.loadJSON"));
        anonymous.resources.add(Resource.createOrFetch("Replies.countAll"));

        anonymous.save();
    }

    /**
     * 初始化普通用户的权限
     */
    private void initNormalUser(UserGroup normalUser) {
        normalUser.resources.add(Resource.createOrFetch("Users.home"));
        normalUser.resources.add(Resource.createOrFetch("Topics.save"));
        normalUser.resources.add(Resource.createOrFetch("Replies.save"));
        normalUser.save();
    }

    /**
     * 读取所有Controller以初始化所有访问资源.
     */
    private void initResources() {
        // 只有第一次初始化的时候才这么做
        if (Resource.count() > 0) return;
        // 获得所以Controller
        Class<?>[] classes =
                new Class<?>[] {Admin.class, Resources.class, Users.class, Application.class,
                        controllers.Users.class};
        // 获得每个Controller中的public static方法,
        for (Class<?> clazz : classes) {
            String name = clazz.getName().replace("controllers.", "");
            Method[] methods = clazz.getDeclaredMethods();
            for (Method m : methods) {
                if (Modifier.isPublic(m.getModifiers()) && Modifier.isStatic(m.getModifiers())) {
                    String action = name + "." + m.getName();
                    Resource resource = Resource.createOrFetch(action, action);
                    resource.save();
                }
            }
        }
    }

    private void initTempData() {
        Board b1 = new Board();
        b1.name = "畅所欲言";
        b1.orderNo = 0;
        b1.save();

        Board b2 = new Board();
        b2.name = "BUG讨论";
        b2.orderNo = 1;
        b2.save();

        Board b3 = new Board();
        b3.name = "功能建议";
        b3.orderNo = 2;
        b3.save();

        Board b4 = new Board();
        b4.name = "代码吐槽";
        b4.orderNo = 3;
        b4.save();
    }
}
