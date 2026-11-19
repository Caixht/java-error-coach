package com.caixht.coach.infra;

import com.caixht.coach.Explanation;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class DeepSeekClient {

    private static final String DEFAULT_URL = "https://api.deepseek.com/chat/completions";

    private final CoachApiClient client = new CoachApiClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private final String url;

    public DeepSeekClient() {
        this(DEFAULT_URL);
    }

    public DeepSeekClient(String url) {
        this.url = url;
        mapper.configure(
                com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public Explanation explain(String stackText) throws IOException, InterruptedException {
        String json = mapper.writeValueAsString(payload(stackText));
        ApiResponse resp = client.post(url, json, LocalConfig.key());

        if (resp.status() != 200) {
            throw new IOException("HTTP " + resp.status() + " " + resp.body());
        }

        var root = mapper.readTree(resp.body());
        String content = root.path("choices").path(0).path("message").path("content").asText();
        if (content.isBlank()) {
            throw new IOException("返回里没有 content");
        }
        return mapper.readValue(content, Explanation.class);
    }

    private Map<String, Object> payload(String stackText) {
        return Map.of(
                "model", "deepseek-chat",
                "messages", List.of(Map.of("role", "user", "content", PROMPT + "\n报错原文：\n" + stackText)),
                "response_format", Map.of("type", "json_object"));
    }

    private static final String PROMPT = """
            你是一个 Java 报错解释助手。学生贴来一段报错堆栈。
            请用中文返回 JSON，只返回 JSON 本身，不要加任何解释，也不要包代码块标记。

            JSON 字段：
            - error_type：错误类型的中文名，例如 空指针
            - plain_explanation：一句话说清发生了什么
            - knowledge_points：数组，2 到 3 个相关知识点，每个是短语
            - likely_line：最可能出错的位置，形如 Main.java:4，不确定就填 null
            - hints：数组，恰好 3 个字符串。第 1 条指出看哪里；第 2 条提示对比什么；第 3 条给方向（用提问结尾）。
            - confidence：0 到 1 之间的小数

            不要提供修复代码，不要写"你可以改成"之类的内容。
            """;
}