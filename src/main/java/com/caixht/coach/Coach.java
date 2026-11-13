package com.caixht.coach;

import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Path;

public class Coach {
    public static void main(String[] args) throws Exception {
        try (var list = Files.list(Path.of("samples"))) {
            for (Path p : list.filter(x -> x.toString().endsWith(".txt"))
                    .sorted()
                    .toList()) {
                System.out.println("=== " + p.getFileName());
                explain(Files.readString(p));
            }
        }
    }
    static void explain(String text) {
        String type = classify(text);
        String where = locate(text);
        System.out.println("报错的类型为: " + type + "->" + describe(type));
        System.out.println("出错的位置在: " + where);
        for (int level = 1; level <= 3; level++) {
            System.out.println(level+"."+ hint(type, where, level));
        }
        System.out.println();
    }

    static String classify(String text) {
        String name = extractExceptionName(text);
        if (name.isEmpty()) return "";
        if (name.equals("java.util.InputMismatchException")) return "输入不匹配";
        if (name.equals("java.lang.NullPointerException")) return "空指针";
        if (name.equals("java.lang.ArrayIndexOutOfBoundsException")) return "数组越界";
        if (name.equals("java.lang.StringIndexOutOfBoundsException") ||
                name.equals("java.lang.IndexOutOfBoundsException")) {
            return "索引越界";
        }
        if (name.equals("java.lang.ClassCastException")) return "类型转换";
        return "未知错误";
    }
    static String locate(String text){
        String[] lines = text.split("\n");
        for (int i = 0; i < lines.length; i++) {
            String cleanLine = lines[i].trim();
            if (cleanLine.startsWith("at ") && !cleanLine.contains("java.")) {
                return cleanLine.substring(cleanLine.indexOf("(") + 1, cleanLine.indexOf(")"));
            }
        }
        return "未知位置";
    }
    static String describe(String type){
        if (type.equals("输入不匹配")){
            return "输入的数据与程序要求的类型不一样";
        } else if (type.equals("空指针")) {
            return "并没有可调用的对象:你正在调用/查看一个未被创建的对象";
        } else if (type.equals("数组越界")) {
            return "访问了不存在的数组下标:代码尝试访问的下标超过了数组的实际大小";
        } else if (type.equals("索引越界")) {
            return "索引超出数据的长度:你访问的位置超出了数据的总长度";
        } else if (type.equals("类型转换")) {
            return "类型转换异常：将对象强转为另一种类型时失败";
        }
        return "未知类型";
    }
    static String hint(String type, String where, int level) {
        String anchor = where.equals("未知位置") ? "报错那行" : where;
        if (type.equals("输入不匹配")) {
            if (level == 1) {
                return "回到 「" + anchor + "」看看它要求程序读进来的是什么类型。";
            }
            if (level == 2) {
                return "你刚才敲进去的是什么类型的数据?——它和这一行要的是同一类东西吗？再输一次同样的内容，看看是不是每次都崩。";
            }
            return "这一行读不出数字时，不会返回一个空值，而是直接把程序打断,你希望程序读到看不懂的东西时怎么办？";
        } else if (type.equals("类型转换")) {
            if (level == 1) {
                return "回到 " + anchor + "看看这一行是不是用括号 () 强制转换了某个对象的数据类型?";
            }
            if (level == 2) {
                return "别只看括号里写了什么，想想这个对象原本被创建时，到底是什么类型？你真的确定它是你输入的类型吗？";
            }
            return "强转失败是因为对象本质不对,如果遇到这种情况,你希望你的程序怎么办?";
        }
        if (level == 1) {
            if (type.equals("空指针"))   return "检查「" + anchor + "」的代码，回想你当时调用/查看的是什么对象。";
            if (type.equals("数组越界")) return "检查「" + anchor + "」的代码，回想你访问的下标是多少。";
            if (type.equals("索引越界")) return "检查「" + anchor + "」的代码，回想你访问的位置是多少。";
        }
        return "这一类的第 " + level + " 级提示还没写,先自己看看" + anchor + "。";
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
