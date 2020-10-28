package com.boot.smartcontactapp.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    // http://localhost:8080/user/index
    @GetMapping("/index")
    public String dashboard(){
        return "normal/user-dashboard";
    }
}