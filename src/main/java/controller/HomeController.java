package controller;

import http.HttpRequest;
import http.HttpResponse;
import webserver.ModelAndView;

public class HomeController {

    public ModelAndView home(HttpRequest httpRequest, HttpResponse httpResponse) {
        ModelAndView modelAndView = new ModelAndView(httpRequest);
        modelAndView.setView("/index.html");

        return modelAndView;
    }
}
