package controller;

import db.Database;
import http.HttpRequest;
import http.HttpResponse;
import model.User;

import java.util.Map;

public class UserController {

    public String createUser(HttpRequest httpRequest, HttpResponse httpResponse) {
        Map<String, String> requestParam = httpRequest.getRequestParam();
        User user = new User(requestParam.get("userId"), requestParam.get("password"), requestParam.get("name"), requestParam.get("email"));
        Database.addUser(user);

        return "redirect:/index.html";
    }

    public String login(HttpRequest httpRequest, HttpResponse httpResponse) {
        Map<String, String> requestParam = httpRequest.getRequestParam();
        String userId = requestParam.get("userId");
        String password = requestParam.get("password");

        String viewName = "redirect:/index.html";
        try {
            User user = Database.findUserById(userId);
            if (!password.equals(user.getPassword())) {
                viewName = "/user/login_failed.html";
            }
        } catch (NullPointerException e) {
            viewName = "/user/login_failed.html";
        }
        return viewName;
    }
}
