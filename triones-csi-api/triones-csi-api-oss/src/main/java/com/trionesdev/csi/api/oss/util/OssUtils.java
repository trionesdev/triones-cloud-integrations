package com.trionesdev.csi.api.oss.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.URLUtil;
import org.apache.commons.lang3.StringUtils;

public class OssUtils {
    public static String joinPrefix(String path, String prefix) {
        if (StringUtils.isEmpty(path)) {
            return "";
        } else if (StringUtils.isBlank(prefix)) {
            return path;
        } else if (!path.toLowerCase().startsWith("http") && !path.toLowerCase().startsWith("https")) {
            String resPath = prefix + "/" + path;
            if (resPath.toLowerCase().startsWith("http") || resPath.toLowerCase().startsWith("https")) {
                return URLUtil.normalize(resPath);
            } else {
                return FileUtil.normalize(resPath);
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
