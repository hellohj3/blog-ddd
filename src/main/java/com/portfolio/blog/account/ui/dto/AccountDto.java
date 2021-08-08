package com.portfolio.blog.account.ui.dto;

import com.portfolio.blog.account.domain.Account;
import com.portfolio.blog.account.domain.Role;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class AccountDto {

    private String accountId;
    private String password;
    private String name;
    private Integer age;
    private String email;

    @Builder
    public AccountDto(String accountId, String password, String name, Integer age, String email) {
        this.accountId = accountId;
        this.password = password;
        this.name = name;
        this.age = age;
        this.email = email;
    }

    // Dto to Entity
    public Account createAccount(String password, Role role) {
        return Account.builder()
                .accountId(this.accountId)
                .password(password)
                .name(this.name)
                .age(this.age)
                .email(this.email)
                .role(role)
                .build();
    }

}
