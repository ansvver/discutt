package controllers.admin;

import java.util.List;

import models.security.Resource;
import models.security.User;
import models.security.UserGroup;
import play.data.validation.Valid;
import controllers.Application;

public class Users extends Application {

    public static void list() {
        List<User> users = User.findAll();
        render(users);
    }

    public static void edit(long id) {
        User user = User.findById(id);
        List<UserGroup> groups = UserGroup.findAll();
        render(user, groups);
    }

    public static void saveUser(@Valid User user) {
        user.save();
        list();
    }

    public static void deleteUser(long id) {
        User.delete("id = ?", id);
        list();
    }

    public static void groups() {
        List<UserGroup> groups = UserGroup.findAll();
        render(groups);
    }

    public static void editGroup(long id) {
        UserGroup group = UserGroup.findById(id);
        List<Resource> resources = Resource.findAll();
        if (group != null) {
            for (Resource resource : resources) {
                if (group.resources.contains(resource)) {
                    resource.checked = true;
                }
            }
        }
        render(group, resources);
    }

    public static void saveGroup(@Valid UserGroup group) {
        group.save();
        groups();
    }

    public static void deleteGroup(long id) {
        UserGroup group = UserGroup.findById(id);
        // 从资源中先删除所有分组, UserGroup和Resource的关联中UserGroup是owner.
        // Owner方可以自动维护关系，被维护方需要手动维护
        group.delete();
        groups();
    }
}
