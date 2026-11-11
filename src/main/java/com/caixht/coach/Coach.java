package com.caixht.coach;

import java.nio.file.Files;
import java.nio.file.Path;

public class Coach {
    public static void main(String[] args) throws Exception {
        String text = Files.readString(Path.of("samples/001-InputMismatchException.txt"));
        System.out.println(classify("java.lang.NullPointerException"));
        System.out.println(classify("java.lang.ArrayIndexOutOfBoundsException: Index 3 out of bounds for length 3"));
        System.out.println(classify("java.lang.IndexOutOfBoundsException: Index 5"));
        System.out.println(classify(text));
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
}
