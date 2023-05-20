package http;

public class StartLine {
    private final int METHOD_INDEX = 0;
    private final int URL_INDEX = 1;

    private String method;
    private String requestUrl;

    public StartLine(String startLine) {
        String[] splitLine = startLine.split(" ");
        method = splitLine[METHOD_INDEX];
        requestUrl = splitLine[URL_INDEX];
    }

    public String getMethod() {
        return method;
    }

    public String getRequestUrl() {
        return requestUrl;
    }
}
