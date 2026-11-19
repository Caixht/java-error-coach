package com.caixht.coach.app;

import com.caixht.coach.Coach;
import com.caixht.coach.Explanation;
import com.caixht.coach.infra.DeepSeekClient;

public class ExplainService {

    private final DeepSeekClient model;

    public ExplainService() {
        this(new DeepSeekClient());
    }

    public ExplainService(DeepSeekClient model) {
        this.model = model;
    }

    public Explanation explain(String stackText) {
        Explanation local = Coach.explain(stackText);   // 永远先算，这是兜底
        try {
            return model.explain(stackText);
        } catch (Exception failed) {
            System.out.println("模型没拿到，用本地规则：" + failed);
            return local;
        }
    }
}