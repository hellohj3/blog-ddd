package com.portfolio.blog.config.security;

import com.portfolio.blog.account.application.service.ResourceService;
import com.portfolio.blog.config.security.common.FormAuthenticationDetailsSource;
import com.portfolio.blog.config.security.factory.UrlResourcesMapFactoryBean;
import com.portfolio.blog.config.security.filter.PermitAllFilter;
import com.portfolio.blog.config.security.handler.CustomAccessDeniedHandler;
import com.portfolio.blog.config.security.handler.CustomAuthenticationFailureHandler;
import com.portfolio.blog.config.security.handler.CustomAuthenticationSuccessHandler;
import com.portfolio.blog.config.security.metaDataSource.UrlSecurityMetadataSource;
import com.portfolio.blog.config.security.provider.CustomAuthenticationProvider;
import com.portfolio.blog.config.security.voter.IpAddressVoter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import java.util.ArrayList;
import java.util.List;

/**
 * Spring Security 관련 설정 클래스
 * 1. URL 접근에 대한 접근권한을 체크
 * 2. 접근권한은 권한계층 구조를 사용
 * 3. 접근권한 정보는 Database 와 연동됨
 *
 * @author 박상재
 * @version 1.0
 */
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final ResourceService resourceService;
    private final FormAuthenticationDetailsSource authenticationDetailsSource;
    private final CustomAuthenticationSuccessHandler authenticationSuccessHandler;
    private final CustomAuthenticationFailureHandler authenticationFailureHandler;

    // permitAll 시켜야하는 자원들
    private String[] permitAllResources = {"/","/login","/posts","/account/login/**","/assets/**","/assets/front/**"};

    @Bean
    // 패스워드 인코더 Bean 생성
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    // 커스텀 인증 provider Bean 성
    public CustomAuthenticationProvider authenticationProvider() {
        return new CustomAuthenticationProvider();
    }

    @Bean
    // 커스텀 메타데이터 Bean 생성
    public UrlSecurityMetadataSource urlSecurityMetadataSource() throws Exception {
        return new UrlSecurityMetadataSource(urlResourcesMapFactoryBean().getObject(), resourceService);
    }

    // 요청에따른 권한 맵핑 정보를 담당할 Bean
    private UrlResourcesMapFactoryBean urlResourcesMapFactoryBean() {
        return new UrlResourcesMapFactoryBean(resourceService);
    }

    @Bean
    // url 기반 권한요청을 위한 Filter 추가영역 - START
    public PermitAllFilter customFilterSecurityInterceptor() throws Exception {
        PermitAllFilter permitAllFilter = new PermitAllFilter(permitAllResources);
        permitAllFilter.setSecurityMetadataSource(urlSecurityMetadataSource());
        permitAllFilter.setAccessDecisionManager(affirmativeBased());
        permitAllFilter.setAuthenticationManager(authenticationManagerBean());
        return permitAllFilter;
    }

    @Bean
    // 접근 결정 관리자
    public AccessDecisionManager affirmativeBased() {
        AffirmativeBased affirmativeBased = new AffirmativeBased(getAccessDecisionVoters());
        return affirmativeBased;
    }

    private List<AccessDecisionVoter<?>> getAccessDecisionVoters() {
        // ip 접근제한 적용 - 가장 먼저 와야됨 이게 우선(ACCESS_GRANTED 가 전에 나와버리면 그다음 보터가 심의 못하기 때문)
        List<AccessDecisionVoter<? extends Object>> accessDecisionVoters = new ArrayList<>();
        accessDecisionVoters.add(new IpAddressVoter(resourceService));

        // Hierarchy 적용
        accessDecisionVoters.add(roleVoter());

        return accessDecisionVoters;
    }

    @Bean
    // 권한 계층 보터 생성
    public AccessDecisionVoter<? extends Object> roleVoter() {
        // 특정 메소드 사용을 위해 RoleHierarchyImpl 타입 사용
        // 이 객체에서 setHierarchy 해서 문자열 세팅해서 넘겨줘야 계층 생성됨
        return new RoleHierarchyVoter(roleHierarchy());
    }

    @Bean
    public RoleHierarchyImpl roleHierarchy() {
        // 특정 메소드 사용을 위해 RoleHierarchyImpl 타입 사용
        return new RoleHierarchyImpl();         // 이 객체에서 setHierarchy 해서 문자열 세팅해서 넘겨줘야함
    }
    // url 기반 구너한요청을 위한 Filter 추가영역 - END

    @Bean
    // 접근 거부 핸들러
    public AccessDeniedHandler accessDeniedHandler() {
        CustomAccessDeniedHandler accessDeniedHandler = new CustomAccessDeniedHandler();
        accessDeniedHandler.setErrorPage("/denied");
        return accessDeniedHandler;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 커스텀 provider 등록 설정
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 로그인 및 접근제한 설정
        http.csrf().disable();

        http
                // 권한필요 명시 - 전부 DB 사용하므로 여기는 이것만 사용
                .authorizeRequests()
            .and()
                // 폼로그인 관련
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login_proc")
                .authenticationDetailsSource(authenticationDetailsSource)
                .defaultSuccessUrl("/")
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
                .permitAll()
            .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler())
            .and()
                .addFilterBefore(customFilterSecurityInterceptor(), FilterSecurityInterceptor.class);
    }
}
