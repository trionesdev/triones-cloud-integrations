package com.trionesdev.csi.api.oss.util;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;

public class OssUtils {
    public static String joinPrefix(String path, String prefix) {
        if (StringUtils.isEmpty(path)) {
            return "";
        } else if (StringUtils.isBlank(prefix)) {
            return path;
        } else if (!path.toLowerCase().startsWith("http") && !path.toLowerCase().startsWith("https")) {
            String resPath = prefix + "/" + path;
            if (resPath.toLowerCase().startsWith("http") || resPath.toLowerCase().startsWith("https")) {
                return FilenameUtils.normalize(resPath).replaceAll("\\\\","/").replace("http:/","http://").replace("https:/","https://");
            } else {
                return FilenameUtils.normalize(resPath);
            }
        } else {
            return path;
        }
    }

    public static String removePrefix(String path, String prefix) {
        if (StringUtils.isEmpty(path)) {
            return "";
        } else if (StringUtils.isBlank(prefix)) {
            return path;
        } else {
            if (!prefix.startsWith("http") && !prefix.startsWith("https")) {
                path = path.replaceAll("^https?://" + prefix, "");
            } else {
                path = path.replaceAll(prefix, "");
            }

            path = path.replaceAll("^/", "");
            return path;
        }
    }

    public static String pathJoin(String... paths) {
        if (paths.length == 0) {
            return "";
        }
        for (int i = 0; i < paths.length; i++) {
            paths[i] = StringUtils.removeEnd(StringUtils.removeStart(paths[i], "/"), "/");
        }
        return StringUtils.join(paths, "/");
    }

}
