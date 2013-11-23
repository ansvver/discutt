package utils.json;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

/**
 * 主要通过JSONUtil来使用JSONObject
 * @author royguo1988@gmail.com(Roy Guo)
 */
public class JSONObject {
    public static final Gson gson = new Gson();

    // 可以是一个List或者一个普通的实体.
    private Object object;
    private Map<String, Object> map = new HashMap<String, Object>();

    public JSONObject(Object object) {
        this.object = object;
    }

    /**
     * 将一个对象的指定字段转换成Key Value的形式，可以继续使用Gson将其直接转换成JSON.
     */
    private Map<String, Object> toMap(Object o, String... fields) {
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
     * 添加一组基本类型字段.
     * @param fields 当前对象下的基本类型字段.
     * @return @JSONObject
     */
    public JSONObject addFields(String... fields) {
        map.putAll(toMap(this.object, fields));
        return this;
    }

    /**
     * 将当前对象的某一个属性当做子元素添加到JSON对象.
     * @param field 当前对象的属性名
     * @param subFields 该级联属性下要加入JSON对象的字段组.
     * @return @JSONObject
     */
    public JSONObject addSubObject(String field, String... subFields) {
        try {
            Field f = object.getClass().getField(field);
            if (f != null) {
                Object value = f.get(object);
                if (value != null) {
                    map.put(field, toMap(value, subFields));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * 将当前对象下的某个子元素数组作为JSON对象的孩子节点.
     * @param fields 子元素数组中的属性
     * @return
     */
    public JSONObject addSubList(String field, String... subFields) {
        try {
            Field f = object.getClass().getField(field);
            if (f != null) {
                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                List<Object> arr = (List) f.get(object);
                for (Object o : arr) {
                    list.add(toMap(o, subFields));
                }
                map.put(field, list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public String toString() {
        return gson.toJson(map);
    }

    public Map<String, Object> toMap() {
        return map;
    }
}
