package com.caixht.coach;

/**
 * 在堆栈里找出「哪个文件、第几行」。
 * 规则：第一条以 "at " 开头、且不含被测程序之外的 JDK 包路径的调用。
 */
public class LocationFinder {

    static String locate(String text) {
        String[] lines = text.split("\n");
        for (int i = 0; i < lines.length; i++) {
            String cleanLine = lines[i].trim();
            if (cleanLine.startsWith("at ") && !cleanLine.contains("java.")) {
                return cleanLine.substring(cleanLine.indexOf("(") + 1, cleanLine.indexOf(")"));
            }
        }
        return "未知位置";
    }
}
