package util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class RequestUtil {
    public static byte[] findResource(String url) throws IOException {
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

    public static Map<String, String> parseQueryString(String queryString) {
        Map<String, String> queryMap = new HashMap<>();

        String[] splitQuery = queryString.split("&");
        for (String query : splitQuery) {
            String[] tokens = query.split("=");
            queryMap.put(tokens[0], tokens[1]);
        }
        return queryMap;
    }
}
