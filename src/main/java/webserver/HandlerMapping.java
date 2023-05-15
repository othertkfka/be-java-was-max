package webserver;

import http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Properties;

public class HandlerMapping {
    private final static Logger logger = LoggerFactory.getLogger(HandlerMapping.class);

    private final int CLASS_INDEX = 0;
    private final int METHOD_INDEX = 1;
    private final String MAPPING_PATH = "/config/mapping.properties";
    private Properties properties = new Properties();

    public String handleRequest(HttpRequest httpRequest) {
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
            Method method = cls.getDeclaredMethod(methodName, HttpRequest.class);
            view = (String) method.invoke(instance, httpRequest);
            logger.debug("View Name : {}", view);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    private String getRequestMapping(String requestUrl) {
        String mapping = "";
        try {
            InputStream reader = getClass().getResourceAsStream(MAPPING_PATH);
            properties.load(reader);
            mapping = properties.getProperty(requestUrl);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return mapping;
    }

}
