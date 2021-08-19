package com.portfolio.blog.common.util;

import java.util.Locale;

/**
 * 라이브러리를 사용하지 않는 커스텀 필터
 */
public class CustomFilter {

    public static boolean xssFilter(String value) {
        value = value.toLowerCase(Locale.ROOT);

        if (value.contains("javascript") || value.contains("vbscript") ||
                value.contains("script") || value.contains("iframe") ||
                value.contains("1=1") || value.contains("select") ||
                value.contains("insert") || value.contains("update") || value.contains("alert")) {
            return true;
        }
        return false;
    }

}
