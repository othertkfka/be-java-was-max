package controller;

import db.Database;
import http.HttpRequest;
import http.HttpResponse;
import model.User;
import webserver.ModelAndView;

import java.util.Map;
import java.util.UUID;

public class UserController {

    public ModelAndView createUser(HttpRequest httpRequest, HttpResponse httpResponse) {
        Map<String, String> requestParam = httpRequest.getRequestParam();
        User user = new User(requestParam.get("userId"), requestParam.get("password"), requestParam.get("name"), requestParam.get("email"));
        Database.addUser(user);

        return new ModelAndView("redirect:/");
    }

    public ModelAndView login(HttpRequest httpRequest, HttpResponse httpResponse) {
        Map<String, String> requestParam = httpRequest.getRequestParam();
        String userId = requestParam.get("userId");
        String password = requestParam.get("password");

        ModelAndView modelAndView = new ModelAndView("redirect:/");
        try {
            User user = Database.findUserById(userId);
            if (!password.equals(user.getPassword())) {
                modelAndView.setView("\"/user/login_failed.html\"");
            }
            httpResponse.setCookie(UUID.randomUUID().toString());
        } catch (NullPointerException e) {
            modelAndView.setView("\"/user/login_failed.html\"");
        }
        return modelAndView;
    }
}
