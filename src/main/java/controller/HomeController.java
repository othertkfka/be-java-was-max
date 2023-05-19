package controller;

import http.HttpRequest;
import http.HttpResponse;

public class HomeController {

    public String home(HttpRequest httpRequest, HttpResponse httpResponse) {
        return "/index.html";
    }
}
