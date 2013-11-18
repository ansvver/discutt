package utils;

import java.io.File;

import play.Play;
import play.libs.Files;

public class FileUtils {

    /**
     * @param dirs
     * @return file system path with slug
     */
    public static String getApplicationPath(String... dirs) {
        String separator = Play.applicationPath.separator;
        StringBuilder builder =
                new StringBuilder(Play.applicationPath.getAbsolutePath() + separator);
        for (String s : dirs) {
            builder.append(s + separator);
        }
        return builder.toString();
    }

    /**
     * @param fileName
     * @return
     */
    public static String getFileExtension(String fileName) {
        String[] segs = fileName.split("\\.");
        return segs[segs.length - 1];
    }

    /**
     * @param file
     * @param dir
     */
    public static void writeTo(File file, String path, String fileName) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        Files.copy(file, new File(path + fileName));
    }
}
