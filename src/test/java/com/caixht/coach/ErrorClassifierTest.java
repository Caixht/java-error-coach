package com.caixht.coach;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ErrorClassifierTest {

    @Test
    void 空指针异常应被识别为空指针() {
        String text = """
                Exception in thread "main" java.lang.NullPointerException
                \tat com.caixht.coach.Main.main(Main.java:6)""";
        assertEquals("空指针", ErrorClassifier.classify(text));
    }
    @Test
    void 输入不匹配应被识别为输入不匹配() {
        String text = "Exception in thread \"main\" java.util.InputMismatchException";
        assertEquals("输入不匹配", ErrorClassifier.classify(text));
    }

    @Test
    void 数组越界应被识别为数组越界() {
        String text = "Exception in thread \"main\" java.lang.ArrayIndexOutOfBoundsException: Index 3 out of bounds";
        assertEquals("数组越界", ErrorClassifier.classify(text));
    }

    @Test
    void 类型转换应被识别为类型转换() {
        String text = "Exception in thread \"main\" java.lang.ClassCastException";
        assertEquals("类型转换", ErrorClassifier.classify(text));
    }

    @Test
    void 编译错误应被识别为编译错误() {
        String text = "Main.java:5: 错误: 不兼容的类型: java.lang.String无法转换为int";
        assertEquals("编译错误", ErrorClassifier.classify(text));
    }

    @Test
    void 不是报错的文本应被识别为未识别() {
        String text = "今天天气不错";
        assertEquals("未识别", ErrorClassifier.classify(text));
    }
    @Test
    void 只有堆栈行没有第一行时不应误判为编译错误() {
        String text = "at com.caixht.coach.Main.main(Main.java:6)";
        assertEquals("未识别", ErrorClassifier.classify(text));
    }
}