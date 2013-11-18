package tags;

import groovy.lang.Closure;

import java.io.PrintWriter;
import java.util.Map;

import models.security.User;
import play.mvc.Scope;
import play.mvc.Scope.Session;
import play.templates.FastTags;
import play.templates.GroovyTemplate.ExecutableTemplate;
import play.templates.JavaExtensions;
import utils.Globals;
import utils.SecurityUtils;

public class PermissionTags extends FastTags {

    public static User currentUser() {
        Session session = Scope.Session.current();
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
     * Usage : <br/>
     * #{hasPermission resource:'/Admin/index'} body #{/hasPermssion}
     */
    public static void _hasPermission(Map<String, String> args, Closure body, PrintWriter out,
            ExecutableTemplate template, int fromLine) {
        if (args == null || args.size() == 0) return;
        String action = args.get("resource");
        User user = currentUser();
        if (!SecurityUtils.checkPermission(user, action, Globals.REQUEST_METHOD.GET.name())) {
            if (!SecurityUtils.checkAnonymous(action, Globals.REQUEST_METHOD.GET.name())) {
                return;
            }
        }
        String html = JavaExtensions.toString(body);
        out.print(html);
    }
}
