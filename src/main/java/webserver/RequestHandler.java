package webserver;

import http.HttpRequest;
import http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Map;

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
            ModelAndView modelAndView = HandlerMapping.handleRequest(httpRequest, httpResponse);

            ViewResolver.resolve(modelAndView, httpResponse);
            responseHeader(dos, httpResponse);
            responseBody(dos, httpResponse);
        } catch (IOException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void responseHeader(DataOutputStream dos, HttpResponse httpResponse) {
        try {
            dos.writeBytes(httpResponse.getStatusLine() + "\r\n");
            Map<String, String> headers = httpResponse.getHeaders();
            for (String header : headers.keySet()) {
                dos.writeBytes(header + ": " + headers.get(header) + "\r\n");
            }
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, HttpResponse httpResponse) {
        try {
            byte[] body = httpResponse.getBody();
            if (body != null) {
                dos.write(body, 0, body.length);
            }
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
