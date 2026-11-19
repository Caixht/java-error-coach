package com.caixht.coach.dev;

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
        System.out.println("key 读到了，长度 " + key.length());
        String url = "https://api.deepseek.com/chat/completions";
        var messages = List.of(Map.of("role", "user", "content", "只回复两个字：收到"));
        var payload = Map.of("model", "deepseek-chat", "messages", messages);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(payload);

        CoachApiClient client = new CoachApiClient();
        ApiResponse resp = client.post(url, json, key);
        System.out.println("状态码 " + resp.status());

        if (resp.status() == 200) {
            var root = mapper.readTree(resp.body());
            String content = root.path("choices").path(0).path("message").path("content").asText();
            System.out.println("模型说：" + content);
        } else {
            System.out.println("返回：" + resp.body());
        }
    }
}