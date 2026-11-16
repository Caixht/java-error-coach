package com.caixht.coach.dev;

import com.caixht.coach.Explanation;
import com.caixht.coach.infra.CoachApiClient;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ApiTry {
    public static void main(String[] args) throws Exception {
        CoachApiClient client = new CoachApiClient();
        String body = client.post("http://localhost:8080/explain", "{\"text\":\"试试\"}");

        ObjectMapper mapper = new ObjectMapper();
        Explanation explanation = mapper.readValue(body, Explanation.class);

        System.out.println(explanation);
        System.out.println("类型: " + explanation.error_type());
        System.out.println("置信度: " + explanation.confidence());
    }
}