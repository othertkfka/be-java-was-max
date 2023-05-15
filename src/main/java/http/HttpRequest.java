package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;

public class HttpRequest {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);

    private StartLine startLine;
    private String[] headers;

    public HttpRequest(BufferedReader br) throws IOException {
        String line = br.readLine();
        logger.debug("start line : {}", line);
        this.startLine = new StartLine(line);

        while(!line.equals("")){
            line = br.readLine();
            logger.debug("request : {}", line);
        }
    }

    public String getRequestUrl() {
        return startLine.getRequestUrl();
    }
}
