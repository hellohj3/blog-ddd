package com.portfolio.blog.account.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleEnum {

    SUPER("ROLE_SUPER", "최고관리자"),
    ADMIN("ROLE_ADMIN", "관리자"),
    MEMBER("ROLE_MEMBER", "사용자");

    private final String key;
    private final String title;

}
