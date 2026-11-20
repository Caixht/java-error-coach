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

    public CoachAnswer explain(String stackText) {
        Explanation local = Coach.explain(stackText);
        try {
            return CoachAnswer.fromModel(model.explain(stackText));
        } catch (Exception failed) {
            return CoachAnswer.fromLocal(local, String.valueOf(failed));
        }
    }
}