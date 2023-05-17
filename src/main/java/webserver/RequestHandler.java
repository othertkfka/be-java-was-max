package webserver;

import java.io.*;
import java.net.Socket;

import http.HttpRequest;
import http.HttpResponse;
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
                DataOutputStream dos = new DataOutputStream(connection.getOutputStream())
        ) {
            HttpRequest httpRequest = new HttpRequest(br);
            HttpResponse httpResponse = new HttpResponse();

            HandlerMapping handlerMapping = new HandlerMapping();
            String url = handlerMapping.handleRequest(httpRequest, httpResponse);

            byte[] body = RequestUtil.findResource(url);

            responseHeader(dos, body.length, getContentType(url), httpResponse);
            responseBody(dos, body);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void responseHeader(DataOutputStream dos, int lengthOfBodyContent, String contentType, HttpResponse httpResponse) {
        try {
            dos.writeBytes("HTTP/1.1 " + httpResponse.getStatusCode() + " " + httpResponse.getStatusText() + "\r\n");
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
