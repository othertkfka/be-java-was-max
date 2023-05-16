package http;

public class StatusLine {
    private String version;
    private int statusCode;
    private String statusText;

    public StatusLine() {
        version = "HTTP/1.1";
        statusCode = 200;
        statusText = "OK";
    }

    public String getStatusLine() {
        return version + " " + statusCode + " " + statusText;
    }
}
