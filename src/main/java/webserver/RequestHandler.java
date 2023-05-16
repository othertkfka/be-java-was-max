package webserver;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import http.HttpRequest;
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

        try (
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                OutputStream out = connection.getOutputStream()
        ) {
            HttpRequest httpRequest = new HttpRequest(br);

            HandlerMapping handlerMapping = new HandlerMapping();
            String url = handlerMapping.handleRequest(httpRequest);

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
