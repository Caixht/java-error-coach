package com.caixht.coach;

import java.nio.file.Files;
import java.nio.file.Path;

public class Coach {
    public static void main(String[] args) throws Exception {
        try (var list = Files.list(Path.of("samples"))) {
            for (Path p : list.filter(x -> x.toString().endsWith(".txt"))
                    .sorted()
                    .toList()) {
                System.out.println("=== " + p.getFileName());
                explain(Files.readString(p));
            }
        }
    }
    public static String explain(String text) {
        String type = ErrorClassifier.classify(text);
        String where = LocationFinder.locate(text);
        StringBuilder result = new StringBuilder();
        result.append("【报错】").append(type).append(" -> ").append(HintBook.describe(type)).append("\n");
        result.append("【位置】").append(where).append("\n");
        for (int level = 1; level <= HintBook.levels(type); level++) {
            result.append(level).append(". ").append(HintBook.hint(type, where, level)).append("\n");
        }
        return result.toString();
    }
}
