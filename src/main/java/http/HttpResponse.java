package http;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private StatusLine statusLine = new StatusLine();
    private Map<String, String> headers = new HashMap<>();
    private byte[] body;

    public String getStatusLine() {
        return statusLine.getStatusLine();
    }

    public int getStatusCode() {
        return statusLine.getStatusCode();
    }

    public String getStatusText() {
        return statusLine.getStatusText();
    }

    public void setStatus(int statusCode, String statusText) {
        statusLine.setStatusCode(statusCode);
        statusLine.setStatusText(statusText);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(String view) {
        headers.put("Content-Type", getContentType(view) + ";charset=UTF-8");
        headers.put("Content-Length", String.valueOf(body.length));
    }

    public void setRedirectHeader(String location) {
        headers.put("Location", location);
        headers.put("Content-Type", "text/html;charset=UTF-8");
        headers.put("Content-Length", "0");
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
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
