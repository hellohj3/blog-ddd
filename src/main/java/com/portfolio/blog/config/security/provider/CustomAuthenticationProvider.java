package com.portfolio.blog.config.security.provider;

import com.portfolio.blog.account.application.service.AccountService;
import com.portfolio.blog.config.security.model.SecurityAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 사용자 인증작업을 수행하는 클래스
 *
 * @author 박상재
 * @version 1.0
 */
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private AccountService accountService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 인증작업
     * AccountService 를 이용하여 인증작업을 진행하고
     * 인증이 되었다면 토큰을 생성하여 리턴합니다
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String accountId = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        SecurityAccount accountDetails = (SecurityAccount) accountService.loadUserByUsername(accountId);

        if (!passwordEncoder.matches(password, accountDetails.getPassword())) {
            throw new BadCredentialsException("패스워드 불일치");
        }

        if (!accountDetails.isEnabled())
            throw new DisabledException("계정 비활성화 상태");

        return new UsernamePasswordAuthenticationToken(accountId, password, accountDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
