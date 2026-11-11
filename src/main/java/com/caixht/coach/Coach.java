package com.caixht.coach;

import java.nio.file.Files;
import java.nio.file.Path;

public class Coach {
    public static void main(String[] args) throws Exception {
        String text = Files.readString(Path.of("samples/001-InputMismatchException.txt"));
        String[] types = {"InputMismatchException", "NullPointerException", "ArrayIndexOutOfBoundsException", "StringIndexOutOfBoundsException"};
        for (int i = 0; i < types.length; i++) {
            String test = types[i].trim();
            explain(test);
        }
        explain(text);

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
        if (text.contains("InputMismatchException")) {
            return "输入不匹配";
        } else if (text.contains("NullPointerException")) {
            return "空指针";
        }else if (text.contains("ArrayIndexOutOfBoundsException")) {
            return"数组越界";
        }else if (text.contains("IndexOutOfBoundsException")) {
            return "索引越界";
        }else {
            return "未知错误";
        }
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
        }
        return "未知类型";
    }
    static String hint(String type, String where, int level) {
        // 位置拿不到时换个锚点，别把"未知位置"拼进句子
        String anchor = where.equals("未知位置") ? "报错的第一行" : where;
        if (type.equals("输入不匹配")) {
            if (level == 1) {
                return "回到 " + anchor + " 那一行，看看它要求程序读进来的是什么类型。";
            }
            if (level == 2) {
                return "你刚才敲进去的是什么类型的数据?——它和这一行要的是同一类东西吗？再输一次同样的内容，看看是不是每次都崩。";
            }
            return "这一行读不出数字时，不会返回一个空值，而是直接把程序打断。你希望程序读到看不懂的东西时怎么办？";
        }
        if (level == 1) {
            if (type.equals("空指针"))   return "检查 " + anchor + " 的代码，回想你当时调用/查看的是什么对象。";
            if (type.equals("数组越界")) return "检查 " + anchor + " 的代码，回想你访问的下标是多少。";
            if (type.equals("索引越界")) return "检查 " + anchor + " 的代码，回想你访问的位置是多少。";
        }
        // 没写的内容就说没写，别编一句看起来像答案的
        return "这一类的第 " + level + " 级提示还没写,先自己看看" + anchor + "。";
    }
}
