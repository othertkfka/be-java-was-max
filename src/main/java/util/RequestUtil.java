package util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class RequestUtil {
    public static byte[] findResources(String url) throws IOException {
        String resourcePath = "static";
        if (url.endsWith(".html")) {
            resourcePath = "templates";
        }
        return Files.readAllBytes(Paths.get("./src/main/resources/" + resourcePath + url));
    }

    public static String separateUrl(String requestLine) {
        String[] splitLine = requestLine.split(" ");
        return splitLine[1];
    }
}
