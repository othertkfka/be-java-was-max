package controller;

import http.HttpRequest;
import model.User;

import java.util.Map;

public class UserController {

    public String createUser(HttpRequest httpRequest) {
        Map<String, String> requestParam = httpRequest.getRequestParam();
        User user = new User(requestParam.get("userId"), requestParam.get("password"), requestParam.get("name"), requestParam.get("email"));

        return "/index.html";
    }
}
