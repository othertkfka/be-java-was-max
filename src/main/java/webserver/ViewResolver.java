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
            if (view.endsWith(".html")) {
                httpResponse.setBody(generateDynamicBody(view, modelAndView.getModel()));
            } else {
                httpResponse.setBody(generateStaticBody(view));
            }
            httpResponse.setHeaders(view);
        }
    }

    private static byte[] generateDynamicBody(String view, Model model) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(RequestUtil.findResource(view)));
        StringBuilder body = new StringBuilder();
        String line;
        while((line = br.readLine()) != null) {
            if (line.trim().matches("\\{\\{.+}}")) {
                String[] tokens = line.trim().replaceAll("\\{\\{|}}", "").split(":");
                String contents = collectContents(br);

                if (tokens[0].equals("if")) {
                    for (String attributeName : model.getAttributeSet()) {
                        Object object = model.getAttribute(attributeName);
                        if (object instanceof String) {
                            contents = contents.replace("{{" + attributeName + "}}", (String)object);
                        }
                    }
                    if ((boolean) model.getAttribute(tokens[1])) {
                        body.append(contents);
                    }
                } else if (tokens[0].equals("^if")) {
                    for (String attributeName : model.getAttributeSet()) {
                        Object object = model.getAttribute(attributeName);
                        if (object instanceof String) {
                            contents = contents.replace("{{" + attributeName + "}}", (String)object);
                        }
                    }
                    if (!(boolean) model.getAttribute(tokens[1])) {
                        body.append(contents);
                    }
                } else if (tokens[0].equals("for")) {

                }
            } else {
                body.append(line);
            }
        }
        br.close();
        return body.toString().getBytes();
    }


    private static byte[] generateStaticBody(String view) throws IOException {
        return Files.readAllBytes(RequestUtil.findResource(view).toPath());
    }

    private static String collectContents(BufferedReader br) throws IOException {
        StringBuilder contents = new StringBuilder();
        String line;
        while((line = br.readLine()) != null) {
            if (line.trim().matches("\\{\\{/\\w+}}")) {
                break;
            }
            contents.append(line);
        }

        return contents.toString();
    }
}
