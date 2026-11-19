package com.caixht.coach.infra;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class LocalConfig {

    private static final Properties PROPS = load();

    private static Properties load() {
        Properties p = new Properties();
        Path file = Path.of("local.properties");
        if (!Files.exists(file)) {
            return p;
        }
        try (InputStream in = Files.newInputStream(file)) {
            p.load(in);
        } catch (Exception e) {
            System.out.println("读 local.properties 失败：" + e.getMessage());
        }
        return p;
    }

    public static String key() {
        return PROPS.getProperty("deepseek.key", "");
    }
}