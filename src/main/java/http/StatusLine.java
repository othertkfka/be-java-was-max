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

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getStatusLine() {
        return version + " " + statusCode + " " + statusText;
    }
}
