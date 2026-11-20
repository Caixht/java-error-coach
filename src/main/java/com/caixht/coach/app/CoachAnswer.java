package com.caixht.coach.app;

import com.caixht.coach.Explanation;

public record CoachAnswer(Explanation explanation, boolean fromModel, String note) {

    public static CoachAnswer fromModel(Explanation e) {
        return new CoachAnswer(e, true, "");
    }

    public static CoachAnswer fromLocal(Explanation e, String why) {
        return new CoachAnswer(e, false, why);
    }
}