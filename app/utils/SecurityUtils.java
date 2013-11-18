package utils;

import models.security.Resource;
import models.security.User;
import models.security.UserGroup;

public class SecurityUtils {
    public static boolean checkPermission(User user, String action, String method) {
        if (user != null) {
            // 如果是管理员则直接通过
            if (Globals.GROUP_ADMIN.equals(user.userGroup.name)) return true;
            for (Resource r : user.userGroup.resources) {
                if (action.equals(r.resource)
                        && (r.method.equals(Globals.REQUEST_METHOD.ALL.name()) || method
                                .equalsIgnoreCase(r.method))) return true;
            }
        }
        return false;
    }

    public static boolean checkAnonymous(String action, String method) {
        UserGroup anonymous = UserGroup.find("name = ? ", Globals.GROUP_ANONYMOUS).first();
        for (Resource r : anonymous.resources) {
            if (action.equals(r.resource)
                    && (r.method.equals(Globals.REQUEST_METHOD.ALL.name()) || method
                            .equalsIgnoreCase(r.method))) {
                return true;
            }
        }
        return false;
    }
}
