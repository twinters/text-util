package be.thomaswinters.wordcounter;

import be.thomaswinters.wordcounter.WordCounter.WordCounterBuilder;
import com.google.common.base.Charsets;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

public class WordCounterIO {

    public static List<String> toStringList(WordCounter wc) {
        return wc.getWordCount().entrySet().stream().map(e -> e.getElement() + "\t" + e.getCount()).collect(Collectors.toList());
    }

    public static void outputTo(WordCounter wc, File outputFile) throws IOException {
        Files.write(outputFile.toPath(), toStringList(wc), Charsets.UTF_8);
    }

    public static WordCounter read(File from) throws IOException {
        WordCounterBuilder b = new WordCounterBuilder();
        Files.readAllLines(from.toPath()).stream()
                .forEach(e -> b.addWord(e.split("\t")[0], Integer.parseInt(e.split("\t")[1])));
        return b.build();
    }
}
