package db;

import java.util.HashMap;
import java.util.Map;

public class SessionStorage {
    private static Map<String, String> sessionStorage = new HashMap<>();

    public static boolean isStored(String sessionId) {
        return sessionStorage.containsKey(sessionId);
    }

    public static String getSessionAttribute(String sessionId) {
        return sessionStorage.get(sessionId);
    }

    public static void setSessionAttribute(String sessionId, String value) {
        sessionStorage.put(sessionId, value);
    }
}
