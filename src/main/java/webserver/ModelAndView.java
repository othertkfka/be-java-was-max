package webserver;

import db.SessionStorage;
import http.HttpRequest;

public class ModelAndView {
    private String view;
    private Model model;

    public ModelAndView(HttpRequest httpRequest) {
        String sessionId = httpRequest.getSession();
        boolean isAuthenticated = SessionStorage.isStored(sessionId);
        String userId = SessionStorage.getSessionAttribute(sessionId);

        Model model = new Model();
        model.addAttribute("isAuthenticated", isAuthenticated);
        model.addAttribute("userId", userId);
        this.model = model;
    }

    public ModelAndView(String view) {
        this.view = view;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public Object getModelAttribute(String attributeName) {
        return model.getAttribute(attributeName);
    }

    public void addModelAttribute(String attributeName, Object attributeValue) {
        model.addAttribute(attributeName, attributeValue);
    }
}
