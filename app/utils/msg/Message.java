package utils.msg;

import utils.Globals;

import com.google.gson.Gson;


public class Message {
    public static final Gson gson = new Gson();
    public static final String MSG_SUCCESS = new Message(Globals.MSG_CODE_SUCCESS, "操作成功").toJSON();
    public static final String MSG_FAIL = new Message(Globals.MSG_CODE_FAIL, "操作失败").toJSON();

    @SuppressWarnings("unused")
    private int code = Globals.MSG_CODE_INFO;
    @SuppressWarnings("unused")
    private String content;

    public Message(int code, String content) {
        this.code = code;
        this.content = content;
    }

    public String toJSON() {
        return gson.toJson(this);
    }

    public static String toJSON(int code, String content) {
        return new Message(code, content).toJSON();
    }
}
