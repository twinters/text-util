package be.thomaswinters.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataLoader {

    public static Stream<String> readLinesStream(URL url) throws IOException {
        BufferedReader in = new BufferedReader(
                new InputStreamReader(url.openStream()));
        return in.lines();
    }
    public static Stream<String> readLinesStream(String s) throws IOException {
        return readLinesStream(ClassLoader.getSystemResource(s));
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

    public static List<Path> getResourceFolderPaths(String folder) throws URISyntaxException, IOException {
        URL urlFolder = ClassLoader.getSystemResource(folder);
        URI uri = urlFolder.toURI();
        Path myPath;
        // When in a jar:
        if (uri.getScheme().equals("jar")) {
            FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap());
            myPath = fileSystem.getPath(folder);
        }
        // In IDE
        else {
            myPath = Paths.get(uri);
        }

        Stream<Path> walk = Files.walk(myPath, 1);
        walk = walk.filter(Files::isRegularFile);
        return walk.collect(Collectors.toList());
    }

    public static List<URL> getResourceFolderURLs(String folder) throws URISyntaxException, IOException {
        return getResourceFolderPaths(folder).stream()
                .map(path -> {
                    try {
                        return path.toUri().toURL();
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }
}
