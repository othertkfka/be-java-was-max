package controller;

import db.SessionStorage;
import http.HttpRequest;
import http.HttpResponse;
import webserver.Model;
import webserver.ModelAndView;

public class HomeController {

    public ModelAndView home(HttpRequest httpRequest, HttpResponse httpResponse) {
        String sessionId = httpRequest.getSession();
        boolean isAuthenticated = SessionStorage.isStored(sessionId);
        String userId = SessionStorage.getSessionAttribute(sessionId);

        ModelAndView modelAndView = new ModelAndView("/index.html");
        Model model = new Model();
        model.addAttribute("isAuthenticated", isAuthenticated);
        model.addAttribute("userId", userId);
        modelAndView.setModel(model);

        return modelAndView;
    }
}
