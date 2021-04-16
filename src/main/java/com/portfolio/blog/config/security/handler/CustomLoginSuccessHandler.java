package com.portfolio.blog.config.security.handler;

import com.portfolio.blog.account.application.service.AccountService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    private String username;
    private final RequestCache requestCache = new HttpSessionRequestCache();
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private final AccountService accountService;

    public CustomLoginSuccessHandler(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        username = request.getParameter("username");
        clearAuthenticationAttribute(request);
        resultRedirectStrategy(request, response, authentication);
    }

    // 로그인 실패 에러 세션 제거
    protected void clearAuthenticationAttribute(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null)
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }

    // 로그인 성공 리다이렉트
    protected void resultRedirectStrategy(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        SavedRequest savedRequest = requestCache.getRequest(request, response);

        if (savedRequest != null) {
            // 다른 경로에서 로그인한 경우
            String targetUrl = savedRequest.getRedirectUrl();
            redirectStrategy.sendRedirect(request, response, targetUrl);
        } else {
            // 디폴트 경로로 보내는 경우
            redirectStrategy.sendRedirect(request, response, "/admin/authentications");
        }
    }

}
