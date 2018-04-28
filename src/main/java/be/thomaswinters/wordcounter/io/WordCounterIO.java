package be.thomaswinters.wordcounter.io;

import be.thomaswinters.util.DataLoader;
import be.thomaswinters.wordcounter.WordCounter;
import be.thomaswinters.wordcounter.WordCounter.Builder;
import com.google.common.base.Charsets;

import java.io.File;
import java.io.IOException;
import java.net.URL;
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
        Builder b = new Builder();
        Files.readAllLines(from.toPath())
                .forEach(e -> add(b, e));
        return b.build();
    }

    private static void add(Builder b, String line) {
        String[] splitted = line.split("\t", 2);
        b.addWord(splitted[0], Integer.parseInt(splitted[1]));
    }

    public static WordCounter read(URL url) throws IOException {
        Builder b = new Builder();
        DataLoader.readLinesStream(url).forEach(e -> add(b, e));
        return b.build();

    }
}
