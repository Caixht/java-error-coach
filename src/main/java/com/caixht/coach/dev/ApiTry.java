package com.caixht.coach.dev;

import com.caixht.coach.Explanation;
import com.caixht.coach.infra.ApiResponse;
import com.caixht.coach.infra.CoachApiClient;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ApiTry {
    public static void main(String[] args) throws Exception {
        CoachApiClient client = new CoachApiClient();
        String url = "http://localhost:8080/explain";
        try {
            ApiResponse resp = client.post(url, "{\"text\":\"试试\"}");
            if (resp.status() == 429) {
                System.out.println("问得太快了，等一会儿再问");
            } else if (resp.status() != 200) {
                System.out.println("服务出了点问题，状态码 " + resp.status());
            } else {
                try {
                    Explanation e = new ObjectMapper().readValue(resp.body(), Explanation.class);
                    System.out.println(e.error_type() + " / " + e.plain_explanation());
                } catch (Exception parseFail) {
                    System.out.println("它返回的东西我看不懂，这次先不给解释");
                }
            }
        } catch (java.net.http.HttpTimeoutException e) {
            System.out.println("等了太久，没等到回应");
        } catch (java.net.ConnectException e) {
            System.out.println("连不上服务，检查一下它开着没有");
        }
    }
}