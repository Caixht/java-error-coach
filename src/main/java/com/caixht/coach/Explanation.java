package com.caixht.coach;

import java.util.List;

public record Explanation(
        String error_type,
        String plain_explanation,
        List<String> knowledge_points,
        String likely_line,
        List<String> hints,
        double confidence) {
}