package io.github.akhil0072.search;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ExactRetrievalEndToEndTest {

    @Test
    void loadsAndSearchesOneHundredDocumentCorpus(@TempDir Path corpusRoot) throws IOException {
        Path documentsDirectory = Files.createDirectories(corpusRoot.resolve("docs"));
        for (int index = 99; index >= 0; index--) {
            String fileName = String.format(Locale.ROOT, "doc-%03d.txt", index);
            String topic = index % 2 == 0 ? "search" : "storage";
            Files.writeString(
                    documentsDirectory.resolve(fileName),
                    "Java " + topic + " corpus item " + index,
                    StandardCharsets.UTF_8);
        }

        SearchEngine engine = SearchEngine.from(DocumentDirectory.load(corpusRoot));
        List<Document> matches = engine.search("search java");

        assertAll(
                () -> assertEquals(50, matches.size()),
                () -> assertEquals("docs/doc-000.txt", matches.get(0).id()),
                () -> assertEquals("docs/doc-098.txt", matches.get(49).id()));
    }
}
