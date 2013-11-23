package utils.json;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 默认情况下只允许使用级联一次的对象，如果有更深级别的级联，可能设计上有问题，需要调整.
 * @author royguo1988@gmail.com
 * @date 2013-11-01
 */
public class JSONUtil {
    private static String toJSON(List<Map<String, Object>> jsons) {
        return JSONObject.gson.toJson(jsons);
    }

    public static JSONObject toJSONForObject(Object o, String... fields) {
        JSONObject json = new JSONObject(o);
        json.addFields(fields);
        return json;
    }

    public static String toJSONForList(List<?> objects, String... fields) {
        return toJSONForListWithSubObject(objects, fields, null, null);
    }

    public static String toJSONForListWithSubObject(List<?> objects, String[] fields,
            String subObjectName, String[] subFields) {
        List<Map<String, Object>> jsons = new ArrayList<Map<String, Object>>();
        for (Object o : objects) {
            JSONObject json = toJSONForObject(o, fields);
            if (subObjectName != null) {
                json.addSubObject(subObjectName, subFields);
            }
            jsons.add(json.toMap());
        }
        return toJSON(jsons);
    }
}
