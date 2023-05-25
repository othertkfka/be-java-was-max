package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.RequestUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HttpRequest {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);

    private StartLine startLine;
    private Map<String, String> headers = new HashMap<>();
    private Map<String, String> requestParam = new HashMap<>();

    public HttpRequest(BufferedReader br) throws IOException {
        createHttpRequest(br);
    }

    public String getRequestUrl() {
        return startLine.getRequestUrl();
    }

    public String getMethod() {
        return startLine.getMethod();
    }

    public String getSession() {
        if (headers.containsKey("Cookie")) {
            String cookie = headers.get("Cookie");
            String[] values = cookie.split(";");
            for (String value : values) {
                String[] tokens = value.trim().split("=");
                if (tokens[0].equals("sid")){
                    return tokens[1];
                }
            }
        }
        return UUID.randomUUID().toString();
    }

    public Map<String, String> getRequestParam() {
        return requestParam;
    }

    private void createHttpRequest(BufferedReader br) throws IOException {
        logger.debug("====Http Request Message====");
        // start line
        String line = br.readLine();
        logger.debug("start line : {}", line);
        this.startLine = new StartLine(line);
        // header
        while(true){
            line = br.readLine();
            logger.debug("header { {} }", line);
            if (line.equals("")) {
                break;
            }
            String[] tokens = line.split(":", 2);
            headers.put(tokens[0], tokens[1].trim());
        }
        // body
        if (headers.containsKey("Content-Length")) {
            parsingBody(br);
        }
    }

    private void parsingBody(BufferedReader br) throws IOException {
        int contentLength = Integer.parseInt(headers.get("Content-Length"));
        char[] buffer = new char[contentLength];
        br.read(buffer);

        String body = String.valueOf(buffer);
        logger.debug("body { {} }", body);
        requestParam = RequestUtil.parseQueryString(body);
    }
}
