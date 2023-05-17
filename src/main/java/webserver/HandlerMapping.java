package webserver;

import http.HttpRequest;
import http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Properties;

public class HandlerMapping {
    private final static Logger logger = LoggerFactory.getLogger(HandlerMapping.class);

    private static final int CLASS_INDEX = 0;
    private static final int METHOD_INDEX = 1;
    private static final String MAPPING_PATH = "/config/mapping.properties";
    private static Properties properties = new Properties();

    public static String handleRequest(HttpRequest httpRequest, HttpResponse httpResponse) {
        String requestUrl = httpRequest.getRequestUrl();
        if (requestUrl.matches(".+\\.[^./]+$")) {
            return requestUrl;
        }

        String[] mapping = getRequestMapping(requestUrl).split("\\.");

        String className = mapping[CLASS_INDEX];
        String methodName = mapping[METHOD_INDEX];
        String view = "";
        try {
            Class<?> cls = Class.forName("controller." + className);
            Constructor<?> constructor = cls.getConstructor(null);
            Object instance = constructor.newInstance();
            Method method = cls.getDeclaredMethod(methodName, HttpRequest.class, HttpResponse.class);
            view = (String) method.invoke(instance, httpRequest, httpResponse);
            logger.debug("View Name : {}", view);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    private static String getRequestMapping(String requestUrl) {
        String mapping = "";
        try {
            InputStream reader = HandlerMapping.class.getResourceAsStream(MAPPING_PATH);
            properties.load(reader);
            mapping = properties.getProperty(requestUrl);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return mapping;
    }

}
