package io.github.akhil0072.search;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public final class DocumentDirectory {
    private DocumentDirectory() {}

    public static List<Document> load(Path root) throws IOException {
        Objects.requireNonNull(root, "root");
        if (!Files.isDirectory(root)) {
            throw new IllegalArgumentException("Document root must be a directory: " + root);
        }

        List<Path> files;
        try (Stream<Path> paths = Files.walk(root)) {
            files = paths.filter(Files::isRegularFile)
                    .sorted(Comparator.comparing(path -> documentId(root, path)))
                    .toList();
        }

        List<Document> documents = new ArrayList<>(files.size());
        for (Path file : files) {
            documents.add(new Document(
                    documentId(root, file),
                    Files.readString(file, StandardCharsets.UTF_8)));
        }
        return List.copyOf(documents);
    }

    private static String documentId(Path root, Path file) {
        return root.relativize(file).toString().replace(File.separatorChar, '/');
    }
}
