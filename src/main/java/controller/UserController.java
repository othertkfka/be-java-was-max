package controller;

import db.Database;
import db.SessionStorage;
import http.HttpRequest;
import http.HttpResponse;
import model.User;
import webserver.Model;
import webserver.ModelAndView;

import java.util.Collection;
import java.util.Map;

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
                modelAndView.setView("/user/login_failed.html");
            }
            // 로그인 성공 세션 저장
            String sessionId = httpRequest.getSession();
            SessionStorage.setSessionAttribute(sessionId, userId);
            httpResponse.setCookie(sessionId);
        } catch (NullPointerException e) {
            modelAndView.setView("/user/login_failed.html");
        }
        return modelAndView;
    }

    public ModelAndView getUserList(HttpRequest httpRequest, HttpResponse httpResponse) {
        String sessionId = httpRequest.getSession();
        ModelAndView modelAndView = new ModelAndView();
        if (SessionStorage.isStored(sessionId)) {
            Collection<User> users = Database.findAll();
            Model model = new Model();
            model.addAttribute("users", users);
            modelAndView.setModel(model);
            modelAndView.setView("/user/list.html");
            return modelAndView;
        }
        modelAndView.setView("/user/login.html");
        return modelAndView;
    }
}
