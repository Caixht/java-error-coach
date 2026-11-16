package com.caixht.coach;
public record Explanation(
        String error_type,
        String plain_explanation,
        double confidence
) {
}