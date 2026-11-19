package com.caixht.coach.dev;

import com.caixht.coach.Explanation;
import com.caixht.coach.infra.ApiResponse;
import com.caixht.coach.infra.CoachApiClient;
import com.caixht.coach.infra.LocalConfig;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

public class ApiTry {

    public static void main(String[] args) throws Exception {
        String key = LocalConfig.key();
        if (key.isBlank()) {
            System.out.println("没找到 key：检查 local.properties 里有没有 deepseek.key");
            return;
        }

        String prompt = """
                你是一个 Java 报错解释助手。学生贴来一段报错堆栈。
                请用中文返回 JSON，只返回 JSON 本身，不要加任何解释，也不要包代码块标记。

                JSON 字段：
                - error_type：错误类型的中文名，例如 空指针
                - plain_explanation：一句话说清发生了什么
                - knowledge_points：数组，2 到 3 个相关知识点，每个是短语
                - likely_line：最可能出错的位置，形如 Main.java:4，不确定就填 null
                - hints：数组，恰好 3 个字符串。
                  第 1 条指出看哪里；第 2 条提示对比什么；第 3 条给方向（用提问结尾）。
                  一级比一级具体。严禁出现可以直接复制的修复代码，严禁出现"你应该改成"。
                - confidence：0 到 1 之间的小数

                不要提供修复代码，不要写"你可以改成"之类的内容。

                报错原文：
                Exception in thread "main" java.lang.NullPointerException: Cannot invoke "String.length()" because "s" is null
                \tat com.caixht.coach.Main.main(Main.java:4)
                """;

        var messages = List.of(Map.of("role", "user", "content", prompt));
        var payload = Map.of(
                "model", "deepseek-chat",
                "messages", messages,
                "response_format", Map.of("type", "json_object"));

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(payload);

        CoachApiClient client = new CoachApiClient();
        ApiResponse resp = client.post("https://api.deepseek.com/chat/completions", json, key);
        System.out.println("状态码 " + resp.status());

        if (resp.status() != 200) {
            System.out.println("返回：" + resp.body());
            return;
        }

        var root = mapper.readTree(resp.body());
        String content = root.path("choices").path(0).path("message").path("content").asText();
        System.out.println("模型返回的原文：" + content);


        mapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Explanation e = mapper.readValue(content, Explanation.class);
        System.out.println("类型：" + e.error_type());
        System.out.println("解释：" + e.plain_explanation());
        System.out.println("知识点：" + e.knowledge_points());
        System.out.println("位置：" + e.likely_line());
        System.out.println("三级提示：");
        for (int i = 0; i < e.hints().size(); i++) {
            System.out.println("  " + (i + 1) + ". " + e.hints().get(i));
        }
        System.out.println("把握：" + e.confidence());
    }
}