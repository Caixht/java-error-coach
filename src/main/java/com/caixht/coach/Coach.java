package com.caixht.coach;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Coach {

    public static void main(String[] args) throws Exception {
        try (var list = Files.list(Path.of("samples"))) {
            for (Path p : list.filter(x -> x.toString().endsWith(".txt")).sorted().toList()) {
                System.out.println("=== " + p.getFileName());
                System.out.println(render(explain(Files.readString(p))));
            }
        }
    }

    /** 只负责"算出结果"，不负责打印 */
    public static Explanation explain(String text) {
        String type = ErrorClassifier.classify(text);
        String where = LocationFinder.locate(text);

        List<String> hints = new ArrayList<>();
        for (int level = 1; level <= HintBook.levels(type); level++) {
            hints.add(HintBook.hint(type, where, level));
        }

        return new Explanation(
                type,
                HintBook.describe(type),
                List.of(),
                where,
                hints,
                1.0);
    }

    /** 只负责"把结果变成文字"，不负责算 */
    public static String render(Explanation e) {
        StringBuilder sb = new StringBuilder();
        sb.append("【报错】").append(e.error_type()).append(" -> ").append(e.plain_explanation()).append("\n");
        sb.append("【位置】").append(e.likely_line()).append("\n");
        for (int i = 0; i < e.hints().size(); i++) {
            sb.append(i + 1).append(". ").append(e.hints().get(i)).append("\n");
        }
        return sb.toString();
    }
}