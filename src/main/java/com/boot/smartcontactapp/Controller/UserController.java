package com.boot.smartcontactapp.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {


    // dashboard home
    @GetMapping("/index")
    public String dashboard(Model model) {
        model.addAttribute("title", "User Dashboard");
        return "dashboard";
    }

}
