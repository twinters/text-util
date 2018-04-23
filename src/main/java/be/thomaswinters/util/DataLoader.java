package be.thomaswinters.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DataLoader {
    public static List<String> readLines(URL url) throws IOException {
        BufferedReader in = new BufferedReader(
                new InputStreamReader(url.openStream()));

        List<String> output = new ArrayList<>();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            output.add(inputLine);
        }
        in.close();
        return output;
    }

    public static List<String> readLines(String s) throws IOException {
        return readLines(ClassLoader.getSystemResource(s));
    }
}
