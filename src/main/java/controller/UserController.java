package controller;

import db.Database;
import http.HttpRequest;
import http.HttpResponse;
import model.User;

import java.util.Map;
import java.util.UUID;

public class UserController {

    public String createUser(HttpRequest httpRequest, HttpResponse httpResponse) {
        Map<String, String> requestParam = httpRequest.getRequestParam();
        User user = new User(requestParam.get("userId"), requestParam.get("password"), requestParam.get("name"), requestParam.get("email"));
        Database.addUser(user);

        return "redirect:/";
    }

    public String login(HttpRequest httpRequest, HttpResponse httpResponse) {
        Map<String, String> requestParam = httpRequest.getRequestParam();
        String userId = requestParam.get("userId");
        String password = requestParam.get("password");

        try {
            User user = Database.findUserById(userId);
            if (!password.equals(user.getPassword())) {
                return "/user/login_failed.html";
            }
            httpResponse.setCookie(UUID.randomUUID().toString());
        } catch (NullPointerException e) {
            return "/user/login_failed.html";
        }
        return "redirect:/";
    }
}
