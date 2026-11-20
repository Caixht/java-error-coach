package com.caixht.coach.infra;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;

public final class LocalConfig {

    private static final Properties PROPS = load();

    private static Properties load() {
        Properties p = new Properties();
        for (Path file : candidates()) {
            if (!Files.exists(file)) {
                continue;
            }
            try (InputStream in = Files.newInputStream(file)) {
                p.load(in);
                return p;
            } catch (Exception e) {
                System.out.println("读 " + file + " 失败：" + e);
            }
        }
        return p;
    }

    private static List<Path> candidates() {
        return List.of(
                Path.of("local.properties"),
                Path.of(System.getProperty("user.home"), ".java-error-coach", "local.properties"));
    }

    public static String key() {
        return PROPS.getProperty("deepseek.key", "");
    }
}