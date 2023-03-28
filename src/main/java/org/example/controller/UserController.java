package org.example.controller;

import org.example.annotation.Controller;
import org.example.annotation.Inject;
import org.example.service.UserService;

// UserController Class => Controller Annotation[Inherit]
@Controller
public class UserController {
    // Filed => UserService
    private final UserService userService;

    // UserController Constructor add <= Inject Annotation[Filed, Constructor, Method]
    @Inject
    public UserController(UserService userService) {
        // userService value
        this.userService = userService;
    }

    // UserService 를 조회하는 getter 를 add
    public UserService getUserService() {
        return userService;
    }
}
