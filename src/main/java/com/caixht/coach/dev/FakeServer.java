package com.caixht.coach.dev;

import com.sun.net.httpserver.HttpServer;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class FakeServer {
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/explain", exchange -> {
            String query = exchange.getRequestURI().getQuery();
            String mode = query == null ? "ok" : query.replace("mode=", "");
            int status = 200;
            String json = """
            {"error_type":"空指针","plain_explanation":"调用了没有创建的对象","confidence":0.9}""";
            if ("slow".equals(mode)) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            } else if ("429".equals(mode)) {
                status = 429;
                json = "{\"error\":\"rate limited\"}";
            } else if ("badjson".equals(mode)) {
                json = "这不是 JSON";
            }
            byte[] body = json.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json; charset=utf-8");
            exchange.sendResponseHeaders(status, body.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(body);
            }
        });
        server.start();
        System.out.println("接口已启动: http://localhost:8080/explain");
    }
}