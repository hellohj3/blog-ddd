package com.portfolio.blog.account.application.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AccountServiceTest {

    @Autowired
    AccountService accountService;

    @Test
    public void Account_조회_테스트() {
        //given
        String accountId1 = "admin";

        //when
        UserDetails findOneUserDetails = accountService.loadUserByUsername(accountId1);

        //then
        assertEquals(findOneUserDetails.getUsername(), accountId1, "ID 조회 실패");
    }

    @Test
    public void Account_예외처리_테스트() {
        //then
        assertThrows(InternalAuthenticationServiceException.class, () -> {
            //given
            String accountId2 = "empty";

            //when
            accountService.loadUserByUsername(accountId2);
        });
    }

}