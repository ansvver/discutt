package utils;

public class Globals {
    public final static String USER_ID = "USER_ID";
    public final static String USER_NICK_NAME = "USER_NICK_NAME";

    // 默认的核心用户组,不能删除
    public final static String GROUP_ANONYMOUS = "ANONYMOUS";
    public final static String GROUP_ADMIN = "ADMIN";
    public final static String GROUP_NORMAL = "NORMAL";

    public static enum REQUEST_METHOD {
        ALL, GET, POST, DELETE, PUT
    };

    // Messages
    public static final String MSG = "MSG";
    public static final int MSG_CODE_FAIL = 0;
    public static final int MSG_CODE_SUCCESS = 1;
    public static final int MSG_CODE_INFO = 2;
    
    public static final String HAS_UNOPENED_MESSAGE = "HAS_UNOPENED_MESSAGE";

}
