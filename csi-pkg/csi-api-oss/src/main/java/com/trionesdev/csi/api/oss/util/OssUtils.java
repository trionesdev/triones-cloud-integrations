package com.trionesdev.csi.api.oss.util;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class OssUtils {

    private OssUtils() {
    }

    /**
     * 将对象路径与 URL 前缀拼接。若 path 已是完整 http(s) 地址则原样返回。
     */
    public static String joinPrefix(String path, String prefix) {
        if (StringUtils.isEmpty(path)) {
            return "";
        }
        if (StringUtils.isBlank(prefix)) {
            return path;
        }
        if (isHttpUrl(path)) {
            return path;
        }
        return normalizeSlashes(joinSegments(prefix, path));
    }

    /**
     * 从完整 URL / 路径中移除前缀，得到对象名。
     */
    public static String removePrefix(String path, String prefix) {
        if (StringUtils.isEmpty(path)) {
            return "";
        }
        if (StringUtils.isBlank(prefix)) {
            return path;
        }

        String result;
        if (isHttpUrl(prefix)) {
            result = path.startsWith(prefix) ? path.substring(prefix.length()) : path.replace(prefix, "");
        } else {
            // path 可能带 scheme，prefix 通常为 host/bucket 等形式
            result = path.replaceFirst("(?i)^https?://" + Pattern.quote(prefix), "");
        }
        return StringUtils.removeStart(result, "/");
    }

    /**
     * 拼接路径片段，自动去除首尾多余斜杠，不修改入参数组。
     */
    public static String pathJoin(String... paths) {
        if (paths == null || paths.length == 0) {
            return "";
        }
        List<String> segments = new ArrayList<>(paths.length);
        for (String path : paths) {
            if (StringUtils.isBlank(path)) {
                continue;
            }
            String segment = StringUtils.removeEnd(StringUtils.removeStart(path.replace('\\', '/'), "/"), "/");
            if (StringUtils.isNotEmpty(segment)) {
                segments.add(segment);
            }
        }
        return StringUtils.join(segments, "/");
    }

    private static boolean isHttpUrl(String value) {
        if (value == null) {
            return false;
        }
        String lower = value.toLowerCase();
        return lower.startsWith("http://") || lower.startsWith("https://");
    }

    private static String joinSegments(String prefix, String path) {
        String left = StringUtils.removeEnd(prefix.replace('\\', '/'), "/");
        String right = StringUtils.removeStart(path.replace('\\', '/'), "/");
        return left + "/" + right;
    }

    /**
     * 合并重复斜杠，同时保留 http://、https:// 中的双斜杠。
     */
    private static String normalizeSlashes(String path) {
        if (StringUtils.isEmpty(path)) {
            return path;
        }
        int schemeEnd = -1;
        if (path.regionMatches(true, 0, "https://", 0, 8)) {
            schemeEnd = 8;
        } else if (path.regionMatches(true, 0, "http://", 0, 7)) {
            schemeEnd = 7;
        }
        if (schemeEnd > 0) {
            return path.substring(0, schemeEnd) + path.substring(schemeEnd).replaceAll("/{2,}", "/");
        }
        return path.replaceAll("/{2,}", "/");
    }

}
