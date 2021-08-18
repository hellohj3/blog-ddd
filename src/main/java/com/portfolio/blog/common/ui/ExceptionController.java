package com.portfolio.blog.common.ui;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class ExceptionController {

    @GetMapping("denied")
    public String accessDenied(@RequestParam(value = "exception", required = false) String exception
            , Model model) throws Exception {
        model.addAttribute("status", "403");
        model.addAttribute("error", "Forbidden");
        model.addAttribute("message", exception);

        return "/error/404";
    }

}
