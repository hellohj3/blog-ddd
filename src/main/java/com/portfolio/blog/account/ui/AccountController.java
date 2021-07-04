package com.portfolio.blog.account.ui;

import com.portfolio.blog.account.infra.AccountRepository;
import com.portfolio.blog.config.security.model.SecurityAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final AccountRepository accountRepository;

    @GetMapping("/login")
    public String form(Model model) {
        model.addAttribute("securityAccount", new SecurityAccount());
        return "login";
    }

    @PostMapping("/admin-login")
    public String login(@AuthenticationPrincipal SecurityAccount securityAccount, Model model) {
        return "redirect:/admin";
    }

}
