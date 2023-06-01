package webserver;

import http.HttpResponse;
import util.RequestUtil;

import java.io.IOException;
import java.nio.file.Files;

public class ViewResolver {

    private static final String REDIRECT_PREFIX = "redirect:";

    public void resolve(ModelAndView modelAndView, HttpResponse httpResponse) throws IOException, IllegalAccessException {
        String view = modelAndView.getView();
        if (view.startsWith(REDIRECT_PREFIX)) {
            httpResponse.setStatus(302, "FOUND");
            httpResponse.setRedirectHeader(view.replace(REDIRECT_PREFIX, ""));
        } else {
            if (view.endsWith(".html")) {
                TemplateEngine templateEngine = new TemplateEngine();
                httpResponse.setBody(templateEngine.generateDynamicResource(view, modelAndView.getModel()));
            } else {
                httpResponse.setBody(generateStaticBody(view));
            }
            httpResponse.setHeaders(view);
        }
    }

    private byte[] generateStaticBody(String view) throws IOException {
        return Files.readAllBytes(RequestUtil.findResource(view).toPath());
    }
}
