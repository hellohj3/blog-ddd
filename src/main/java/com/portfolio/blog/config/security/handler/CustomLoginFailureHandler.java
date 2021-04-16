package com.portfolio.blog.config.security.handler;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomLoginFailureHandler implements AuthenticationFailureHandler {

    private String errorMsg;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // 로그인 정보 세팅
        request.setAttribute("username", request.getParameter("username"));

        // 에러메세지 세팅
        if (exception instanceof BadCredentialsException
                || exception instanceof InternalAuthenticationServiceException) {
            errorMsg = "계정정보가 일치하지 않습니다.";
        } else if (exception instanceof DisabledException) {
            errorMsg = "비활성화된 계정입니다. 관리자에게 문의해주세요.";
        } else if (exception instanceof CredentialsExpiredException) {
            errorMsg = "알수 없는 이유로 로그인에 실패하였습니다. 관리자에게 문의하세요.";
        }
        request.setAttribute("loginErrorMsg", errorMsg);

        request.getRequestDispatcher("/admin/login?error").forward(request, response);
    }

}
