package util;

import java.io.File;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class RequestUtil {
    public static File findResource(String url) {
        String resourcePath = "static";
        if (url.endsWith(".html")) {
            resourcePath = "templates";
        }

        if (url.equals("/")) {
            resourcePath = "templates";
            url = "/index.html";
        }

        return new File("./src/main/resources/" + resourcePath + url);
    }

    public static String separateUrl(String requestLine) {
        String[] splitLine = requestLine.split(" ");
        return splitLine[1];
    }

    public static Map<String, String> parseQueryString(String queryString) {
        Map<String, String> queryMap = new HashMap<>();

        String[] splitQuery = decode(queryString).split("&");
        for (String query : splitQuery) {
            String[] tokens = query.split("=");
            queryMap.put(tokens[0], tokens[1]);
        }
        return queryMap;
    }

    public static String decode(String text) {
        return URLDecoder.decode(text, StandardCharsets.UTF_8);
    }
}
