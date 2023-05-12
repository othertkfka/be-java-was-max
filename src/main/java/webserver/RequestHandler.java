package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.RequestUtil;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            String line = br.readLine();
            logger.debug("request line : {}", line);
            String requestUrl = RequestUtil.separateUrl(line);

            String[] tokens = requestUrl.split("\\?");
            String url = tokens[0];
            if (tokens.length > 1) {
                Map<String, String> queryMap = RequestUtil.parseQueryString(tokens[1]);
                User user = new User(queryMap.get("userId"), queryMap.get("password"), queryMap.get("name"), queryMap.get("email"));
            }

            while(!line.equals("")){
                line = br.readLine();
                logger.debug("request : {}", line);
            }

            DataOutputStream dos = new DataOutputStream(out);
            byte[] body = RequestUtil.findResource(url);

            response200Header(dos, body.length, getContentType(url));
            responseBody(dos, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String contentType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + contentType + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private String getContentType(String url) {
        String contentType = "";
        if (url.endsWith(".html")) {
            contentType = "text/html";
        } else if (url.endsWith(".css")) {
            contentType = "text/css";
        } else if (url.endsWith(".js")) {
            contentType = "application/javascript";
        } else if (url.endsWith(".ico")) {
            contentType = "image/x-icon";
        } else if (url.endsWith(".png")) {
            contentType = "image/png";
        } else if (url.endsWith(".jpg")) {
            contentType = "image/jpg";
        }
        return contentType;
    }
}
