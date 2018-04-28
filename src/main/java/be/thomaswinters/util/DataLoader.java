package be.thomaswinters.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
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


    public static List<String> readLines(String s) throws IOException {
        return readLines(ClassLoader.getSystemResource(s));
    }
}
