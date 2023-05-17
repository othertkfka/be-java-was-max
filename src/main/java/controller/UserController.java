package controller;

import http.HttpRequest;
import http.HttpResponse;
import model.User;

import java.util.Map;

public class UserController {

    public String createUser(HttpRequest httpRequest, HttpResponse httpResponse) {
        Map<String, String> requestParam = httpRequest.getRequestParam();
        User user = new User(requestParam.get("userId"), requestParam.get("password"), requestParam.get("name"), requestParam.get("email"));

        return "redirect:/index.html";
    }
}
