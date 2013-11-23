package utils;

import models.security.Resource;
import models.security.User;
import models.security.UserGroup;

/**
 * 常用几个的权限控制工具.
 * @author royguo1988@gmail.com
 */
public class SecurityUtils {
    /**
     * 检查用户是否有某资源的访问权限.
     * @param user 用户
     * @param action 资源
     * @param method 资源访问方式
     * @return
     */
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

    /**
     * 检查匿名用户是否拥有某资源的访问权限.
     * @param action
     * @param method
     * @return
     */
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
