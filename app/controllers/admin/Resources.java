package controllers.admin;

import java.util.List;

import models.security.Resource;
import models.security.UserGroup;
import play.data.validation.Valid;
import utils.Globals;
import utils.Globals.REQUEST_METHOD;
import controllers.Application;

/**
 * @author royguo1988@gmail.com(Roy Guo)
 */
public class Resources extends Application {

    public static void list() {
        List<Resource> resources = Resource.findAll();
        render(resources);
    }

    public static void edit(long id) {
        Resource resource = null;
        if (id > 0) {
            resource = Resource.findById(id);
        }
        REQUEST_METHOD[] methods = Globals.REQUEST_METHOD.values();
        render(resource, methods);
    }

    public static void save(@Valid Resource resource) {
        resource.save();
        list();
    }

    public static void delete(long id) {
        if (id > 0) {
            Resource r = Resource.findById(id);
            // 从资源中先删除所有分组, UserGroup和Resource的关联中UserGroup是owner.
            // Owner方可以自动维护关系，被维护方需要手动维护
            for (UserGroup group : r.userGroups) {
                group.resources.remove(r);
                group.save();
            }
            r.delete();
        }
        list();
    }
}
