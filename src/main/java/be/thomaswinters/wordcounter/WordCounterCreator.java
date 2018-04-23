package be.thomaswinters.wordcounter;

import be.thomaswinters.wordcounter.WordCounter.WordCounterBuilder;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableSet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class WordCounterCreator {
    private final Set<File> files;

    public WordCounterCreator(Collection<? extends File> files) {
        this.files = ImmutableSet.copyOf(files);
    }

    public List<String> readLines(File file) {
        List<String> lines = new ArrayList<>();
        Path path = file.toPath();
        try {
            lines = Files.readAllLines(path, Charsets.UTF_8);
            System.out.println("SUCCESS:" + path);
        } catch (IOException e1) {
            try {
                lines = Files.readAllLines(path, Charsets.UTF_16);
                System.out.println("SUCCESS:" + path);
            } catch (IOException e2) {
                e2.printStackTrace();
                System.out.println("FAIL:" + path);
            }
        }
        return lines;
    }

    public WordCounter createWordCounter() {
        WordCounterBuilder b = new WordCounterBuilder();

        for (File file : files) {
            List<String> lines = readLines(file);
            b.add(lines);
        }

        return b.build();
    }

    public void create(File outputFile) throws IOException {
        WordCounter wc = createWordCounter();


        WordCounterIO.outputTo(WordCounter.filterMininum(wc, 2), outputFile);

    }

    public static void main(String[] args) throws IOException {
        new WordCounterCreator(Arrays.asList(new File("textfiles-com").listFiles())).create(new File("output.txt"));
    }

}
