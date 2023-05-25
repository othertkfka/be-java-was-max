package webserver;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Model {
    private Map<String, Object> model = new HashMap<>();

    public Object getAttribute(String attributeName) {
        return model.get(attributeName);
    }

    public void addAttribute(String attributeName, Object attributeValue) {
        model.put(attributeName, attributeValue);
    }

    public Set<String> getAttributeSet() {
        return model.keySet();
    }
}
