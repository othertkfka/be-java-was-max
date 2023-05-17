package http;

public enum ContentType {
    HTML("text/html", ".html"),
    CSS("text/css", ".css"),
    JS("application/javascript", ".js"),
    ICO("image/x-icon", ".ico"),
    PNG("image/png", ".png"),
    JPG("image/jpg", ".jpg");

    private String type;
    private String extension;

    ContentType(String type, String extension) {
        this.type = type;
        this.extension = extension;
    }

    public static String getContentType(String url) {
        ContentType[] types = values();
        for(ContentType contentType : types) {
            if (url.endsWith(contentType.extension)) {
                return contentType.type;
            }
        }
        return null;
    }

}
