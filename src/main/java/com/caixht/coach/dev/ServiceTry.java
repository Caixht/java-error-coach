package com.caixht.coach.dev;

import com.caixht.coach.Coach;
import com.caixht.coach.app.CoachAnswer;
import com.caixht.coach.app.ExplainService;
import com.caixht.coach.infra.DeepSeekClient;

import java.nio.file.Files;
import java.nio.file.Path;

public class ServiceTry {
    public static void main(String[] args) throws Exception {
        String text = Files.readString(Path.of("samples/002-NullPointerException.txt"));
        String url = args.length > 0 ? args[0] : "https://api.deepseek.com/chat/completions";

        ExplainService service = new ExplainService(new DeepSeekClient(url));
        CoachAnswer answer = service.explain(text);
        System.out.println(Coach.render(answer.explanation()));
    }
}