package io.github.akhil0072.search;

import java.util.Objects;

public record Document(String id, String content) {

    public Document {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(content, "content");
    }
}
