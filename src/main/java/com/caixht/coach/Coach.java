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

    static void explain(String text) {
        String type = ErrorClassifier.classify(text);
        String where = LocationFinder.locate(text);
        System.out.println("报错的类型为: " + type + "->" + HintBook.describe(type));
        System.out.println("出错的位置在: " + where);
        for (int level = 1; level <= HintBook.levels(type); level++) {
            System.out.println(level + "." + HintBook.hint(type, where, level));
        }
        System.out.println();
    }
}
