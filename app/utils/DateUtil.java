package utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author royguo1988@gmail.com(Roy Guo)
 */
public class DateUtil {

    public static String format(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }

}
