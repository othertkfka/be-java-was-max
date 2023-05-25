package webserver;

import http.HttpResponse;
import util.RequestUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;

public class ViewResolver {

    private static final String REDIRECT_PREFIX = "redirect:";

    public static void resolve(ModelAndView modelAndView, HttpResponse httpResponse) throws IOException {
        String view = modelAndView.getView();
        if (view.startsWith(REDIRECT_PREFIX)) {
            httpResponse.setStatus(302, "FOUND");
            httpResponse.setRedirectHeader(view.replace(REDIRECT_PREFIX, ""));
        } else {
            httpResponse.setBody(generateDynamicBody(view));
            httpResponse.setHeaders(view);
        }
    }

    private static byte[] generateDynamicBody(String view) throws IOException {
        // 확장자가 html인 경우 동적 생성
        if (view.endsWith(".html")){
            BufferedReader br = new BufferedReader(new FileReader(RequestUtil.findResource(view)));
            StringBuilder body = new StringBuilder();
            String line = "";
            while((line = br.readLine()) != null) {
                body.append(line);
            }
            br.close();
            return body.toString().getBytes();
        }
        return Files.readAllBytes(RequestUtil.findResource(view).toPath());
    }
}
