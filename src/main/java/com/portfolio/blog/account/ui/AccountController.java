package com.portfolio.blog.account.ui;

import com.portfolio.blog.account.application.service.AccountService;
import com.portfolio.blog.account.domain.Account;
import com.portfolio.blog.account.ui.dto.AccountDto;
import com.portfolio.blog.config.security.model.SecurityAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/login")
    public String form(Model model) {
        model.addAttribute("securityAccount", new SecurityAccount());
        return "login";
    }

    @GetMapping("/account")
    public String formAccount(Model model) {
        model.addAttribute("account"
                , AccountDto.builder()
                        .accountId("")
                        .password("")
                        .name("")
                        .age(null)
                        .email("")
                        .build());

        return "join";
    }

    @PostMapping("/account")
    public String createAccount(Model model, @Valid AccountDto accountDto) {
        Account account = accountService.saveAccount(accountDto);
        String result = "회원가입이 완료되었습니다.\n로그인해 주세요.";

        if (account == null) {
            result = "회원가입에 실패하였습니다.\n관리자에게 문의해 주세요.";
        }

        model.addAttribute("securityAccount", new SecurityAccount());
        model.addAttribute("resultMsg", result);

        return "login";
    }

    @GetMapping("/account/{id}")
    @ResponseBody
    public String findAccount(@PathVariable("id") String accountId, Model model) {
        return accountService.findAccount(accountId) ? "fail" : "pass";
    }

}
