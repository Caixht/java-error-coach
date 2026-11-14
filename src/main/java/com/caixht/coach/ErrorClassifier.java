package com.caixht.coach;

/**
 * 判断这段报错属于哪一类。
 * 先分运行期 / 编译期 / 未识别，再在运行期里按异常全名匹配。
 */
public class ErrorClassifier {

    static String classify(String text) {
        String name = extractExceptionName(text);
        if (name != null && !name.isEmpty()) {
            if (name.equals("java.util.InputMismatchException")) return "输入不匹配";
            if (name.equals("java.lang.NullPointerException")) return "空指针";
            if (name.equals("java.lang.ArrayIndexOutOfBoundsException")) return "数组越界";
            if (name.equals("java.lang.StringIndexOutOfBoundsException") ||
                    name.equals("java.lang.IndexOutOfBoundsException")) {
                return "索引越界";
            }
            if (name.equals("java.lang.ClassNotFoundException")) return "类找不到";
            if (name.equals("java.lang.ClassCastException")) return "类型转换";
            return "未知运行期异常";
        }
        if (text.contains("错误:") || text.contains("error:") || text.contains("java:")) {
            return "编译错误";
        }
        return "未识别";
    }
    static String extractExceptionName(String text) {
        String firstLine = text.split("\n")[0];
        String startAnchor = "in thread \"";
        int startIndex = firstLine.indexOf(startAnchor);
        if (startIndex == -1) return "";
        int rightQuoteIndex = firstLine.indexOf("\"", startIndex + startAnchor.length());
        if (rightQuoteIndex == -1) return "";
        int currentIndex = rightQuoteIndex + 1;
        while (currentIndex < firstLine.length() && firstLine.charAt(currentIndex) == ' ') {
            currentIndex++;
        }
        int colonIndex = firstLine.indexOf(":", currentIndex);
        if (colonIndex != -1) {
            return firstLine.substring(currentIndex, colonIndex).trim();
        } else {
            return firstLine.substring(currentIndex).trim();
        }
    }
}
