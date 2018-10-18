package com.business.exchange.controller;

import com.business.exchange.domain.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @RequestMapping(value = "valid", method = RequestMethod.GET)
    public String valid(String userName, String password) {
        //todo
        return "";
    }

    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public String getUserInfo(@RequestParam("employeeID") String employeeID) {
        //todo
        return "";
    }

    @RequestMapping(value = "/modify", method = RequestMethod.GET)
    public String modifyUserInfo(@RequestParam("user") User user) {
        //todo
        return "";
    }

    @RequestMapping(value = "/password", method = RequestMethod.GET)
    public String modifyPassword(@RequestParam("id") int userId,
                                 @RequestParam("oldPwd") String oldPassword,
                                 @RequestParam("newPwd") String newPassword) {
        //todo
        return "";
    }
}
