package com.portfolio.blog.config.security.handler;

import com.portfolio.blog.account.domain.RoleEnum;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    // 로그인 성공 후, 핸들링하는 클래스

    private RequestCache requestCache = new HttpSessionRequestCache();
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // authentication = 인증 성공한 객체

        SavedRequest savedRequest = requestCache.getRequest(request, response);
        setDefaultTargetUrl("/");

        if (savedRequest != null) {
            // 다른자원에서 접근한경우
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            String targetUrl = savedRequest.getRedirectUrl();

            for (GrantedAuthority authority : authorities)
                redirectStrategy.sendRedirect(request, response, (RoleEnum.MEMBER.getKey().equals(authority.getAuthority()))
                        ? getDefaultTargetUrl() : targetUrl);
        } else {
            // 바로 인증으로 접근한경우
            redirectStrategy.sendRedirect(request, response, getDefaultTargetUrl());
        }
    }
}
