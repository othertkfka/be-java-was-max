package controller;

import http.HttpRequest;
import http.HttpResponse;
import webserver.ModelAndView;

public class HomeController {

    public ModelAndView home(HttpRequest httpRequest, HttpResponse httpResponse) {
        return new ModelAndView("/index.html");
    }
}
