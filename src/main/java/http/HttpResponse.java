package http;

public class HttpResponse {
    private StatusLine statusLine = new StatusLine();

    public void setRedirect() {
        statusLine.setStatusCode(302);
    }

    public int getStatusCode() {
        return statusLine.getStatusCode();
    }

    public String getStatusText() {
        return statusLine.getStatusText();
    }
}
