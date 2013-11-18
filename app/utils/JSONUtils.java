package utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

public class JSONUtils {

    public static final Gson gson = new Gson();

    /**
     * 将一个对象的指定字段转换成Key Value的形式，可以继续使用Gson将其直接转换成JSON.
     */
    public static Map<String, Object> toMap(Object o, String... fields) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        for (String f : fields) {
            try {
                Field field = o.getClass().getField(f);
                map.put(f, field.get(o));
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return map;
    }

    /**
     * 将一个普通的Object转换为JSON.
     * @param o
     * @param fields
     * @return
     */
    public static String toJSON(Object o, String... fields) {
        return gson.toJson(toMap(o, fields));
    }

    /**
     * 将列表转换为JSON数组, 其中fields表示数组内的字段.
     * @param objects
     * @param fields
     * @return
     */
    public static String toJSONForList(List<?> objects, String... fields) {
        ArrayList<Object> list = new ArrayList<Object>();
        for (Object o : objects) {
            list.add(toMap(o, fields));
        }
        return gson.toJson(list);
    }

    /**
     * 把一个List转换成JSON数组的List，可以用来直接转换成JSON Object.
     * <b/>通常在把复杂对象转换成JSON字符串的时候会用得到.
     */
    public static List<Object> toJSONListForList(List<?> objects, String... fields) {
        ArrayList<Object> list = new ArrayList<Object>();
        for (Object o : objects) {
            list.add(toMap(o, fields));
        }
        return list;
    }
}
