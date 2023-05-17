package webserver;

import http.HttpResponse;
import util.RequestUtil;

import java.io.IOException;

public class ViewResolver {

    private static final String REDIRECT_PREFIX = "redirect:";

    public static void resolve(String view, HttpResponse httpResponse) throws IOException {
        if (view.startsWith(REDIRECT_PREFIX)) {
            httpResponse.setStatus(302, "FOUND");
            httpResponse.setRedirectHeader(view.replace(REDIRECT_PREFIX, ""));
        } else {
            httpResponse.setBody(RequestUtil.findResource(view));
            httpResponse.setHeaders(view);
        }
    }
}
