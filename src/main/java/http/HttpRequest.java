package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);

    private StartLine startLine;
    private Map<String, String> headers = new HashMap<>();

    public HttpRequest(BufferedReader br) throws IOException {
        createHttpRequest(br);
    }

    public String getRequestUrl() {
        return startLine.getRequestUrl();
    }

    public String getMethod() {
        return startLine.getMethod();
    }

    private void createHttpRequest(BufferedReader br) throws IOException {
        logger.debug("====Http Request Message====");
        String line = br.readLine();
        logger.debug("start line : {}", line);
        this.startLine = new StartLine(line);

        while(true){
            line = br.readLine();
            logger.debug("header{ {} }", line);
            if (line.equals("")) {
                break;
            }
            String[] tokens = line.split(":", 2);
            headers.put(tokens[0], tokens[1].trim());
        }
    }
}
