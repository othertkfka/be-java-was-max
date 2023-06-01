package webserver;

import util.RequestUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;

public class TemplateEngine {

    private static final String TEMPLATE_START_REGEX = "\\{\\{.+}}";
    private static final String TEMPLATE_END_REGEX = "\\{\\{/\\w+}}";

    public byte[] generateDynamicResource(String view, Model model) throws IOException, IllegalAccessException {
        BufferedReader br = new BufferedReader(new FileReader(RequestUtil.findResource(view)));
        StringBuilder body = new StringBuilder();
        String line;
        while((line = br.readLine()) != null) {
            if (line.trim().matches(TEMPLATE_START_REGEX)) {
                body.append(renderTemplateFormat(line, br, model));
            } else {
                body.append(line);
            }
        }
        br.close();
        return body.toString().getBytes();
    }

    private String renderTemplateFormat(String line, BufferedReader br, Model model) throws IOException, IllegalAccessException {
        String[] identifier = line.trim().replaceAll("\\{\\{|}}", "").split(":");
        String contents = collectInnerContents(br);
        String result = "";
        if (identifier[0].equals("for")) {
            result = renderForStructure(model, identifier[1], contents);
        } else {
            result = renderIfStructure(model, identifier, contents);
        }
        return result;
    }

    private String renderForStructure(Model model, String identifier, String contents) throws IllegalAccessException {
        Collection<Object> list = (Collection<Object>)model.getAttribute(identifier);
        StringBuilder renderResult = new StringBuilder();
        for (Object object : list) {
            Class<?> cls = object.getClass();
            String replaceContents = contents;
            for(Field field : cls.getDeclaredFields()) {
                field.setAccessible(true);
                replaceContents = replaceContents.replace("{{" + field.getName() + "}}", (String)field.get(object));
            }
            renderResult.append(replaceContents);
        }
        return renderResult.toString();
    }

    private String renderIfStructure(Model model, String[] identifier, String contents) {
        contents = replaceAttribute(model, contents);
        boolean ifOrNotIf = true;
        if (identifier[0].equals("^if")) {
            ifOrNotIf = false;
        }
        if (ifOrNotIf == (boolean) model.getAttribute(identifier[1])) {
            return contents;
        }
        return "";
    }

    private String replaceAttribute(Model model, String contents) {
        for (String attributeName : model.getAttributeSet()) {
            Object object = model.getAttribute(attributeName);
            if (object instanceof String) {
                contents = contents.replace("{{" + attributeName + "}}", (String)object);
            }
        }
        return contents;
    }

    private String collectInnerContents(BufferedReader br) throws IOException {
        StringBuilder contents = new StringBuilder();
        String line;
        while((line = br.readLine()) != null) {
            if (line.trim().matches(TEMPLATE_END_REGEX)) {
                break;
            }
            contents.append(line);
        }

        return contents.toString();
    }
}
