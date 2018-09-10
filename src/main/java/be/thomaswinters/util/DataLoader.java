package be.thomaswinters.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataLoader {

    public static Stream<String> readLinesStream(URL url) throws IOException {
        BufferedReader in = new BufferedReader(
                new InputStreamReader(url.openStream()));
        return in.lines();
    }

    public static List<String> readLines(URL url) throws IOException {
        return readLinesStream(url).collect(Collectors.toList());
    }

    public static List<String> readLinesUnchecked(URL url) {
        try {
            return readLines(url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> readLines(String s) throws IOException {
        return readLines(ClassLoader.getSystemResource(s));
    }

    public static List<String> readLinesUnchecked(String s) {
        try {
            return readLines(ClassLoader.getSystemResource(s));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<File> getResourceFolderFiles(String folder) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource(folder);
        assert url != null;
        String path = url.getPath();
        return Arrays.asList(Objects.requireNonNull(new File(path).listFiles()));
    }

    public static List<URL> getResourceFolderUrls(String folder) {
        return getResourceFolderFiles(folder)
                .stream()
                .map(file -> {
                    try {
                        return file.toURI().toURL();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

}
