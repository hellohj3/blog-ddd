package com.portfolio.blog.config.security;

import com.portfolio.blog.account.application.service.AccountService;
import com.portfolio.blog.account.application.service.ResourceService;
import com.portfolio.blog.config.security.factory.UrlResourcesMapFactoryBean;
import com.portfolio.blog.config.security.filter.PermitAllFilter;
import com.portfolio.blog.config.security.handler.CustomLoginFailureHandler;
import com.portfolio.blog.config.security.handler.CustomLoginSuccessHandler;
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
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.util.Arrays;
import java.util.List;

/**
 * Spring Security 관련 설정 클래스
 *
 * @author 박상재
 * @version 1.0
 */
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AccountService accountService;
    private final ResourceService resourceService;
    private final String[] permitAllPattern = {"/admin/login","/admin/login/process","/admin/assets/**","/oauth2/**"};

    /**
     * PasswordEncoder
     * 패스워드 암호화 및 매칭을 수행할 Bean
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Custom FilterSecurityInterceptor
     * FilterSecurityInterceptor 전에 위치해서 자원에대한
     * 설정된 자원에 대한 권한검사를 통과시키는 필터 Bean
     */
    /*@Bean
    public PermitAllFilter permitAllFilter() throws Exception {
        PermitAllFilter permitAllFilter = new PermitAllFilter(permitAllPattern);
        permitAllFilter.setAccessDecisionManager(affirmativeBased(resourceService));
        permitAllFilter.setSecurityMetadataSource(urlSecurityMetadataSource(resourceService));
        permitAllFilter.setRejectPublicInvocations(false);

        return permitAllFilter;
    }*/

    @Bean
    public FilterSecurityInterceptor customFilterSecurityInterceptor() throws Exception {
        FilterSecurityInterceptor filterSecurityInterceptor = new FilterSecurityInterceptor();
        filterSecurityInterceptor.setSecurityMetadataSource(urlSecurityMetadataSource(resourceService));
        filterSecurityInterceptor.setAccessDecisionManager(affirmativeBased(resourceService));
        filterSecurityInterceptor.setAuthenticationManager(authenticationManagerBean());

        return filterSecurityInterceptor;
    }

    /** Authorization processing object
     * url 맵핑정보를 기반으로 url 권한정보를 확인하는 Bean
     */
    @Bean
    public FilterInvocationSecurityMetadataSource urlSecurityMetadataSource(ResourceService resourceService) {
        return new UrlSecurityMetadataSource(urlResourcesMapFactoryBean(resourceService).getObject(),resourceService);
    }

    /** UrlResourcesMapFactoryBean
     * 데이터베이스를통해 url 매핑정보를 생성하는 Bean
     */
    @Bean
    public UrlResourcesMapFactoryBean urlResourcesMapFactoryBean(ResourceService resourceService){
        UrlResourcesMapFactoryBean urlResourcesMapFactoryBean = new UrlResourcesMapFactoryBean();
        urlResourcesMapFactoryBean.setResourcesService(resourceService);
        return urlResourcesMapFactoryBean;
    }

    /** AccessDecisionManager
     * 인증된 사용자의 접근 혀용여부 판단 Bean
     */
    private AccessDecisionManager affirmativeBased(ResourceService resourceService) {
        AffirmativeBased accessDecisionManager = new AffirmativeBased(getAccessDecisionVoters(resourceService));
        accessDecisionManager.setAllowIfAllAbstainDecisions(false);
        return accessDecisionManager;
    }

    /** AccessDecisionVoters
     *  AccessDecisionManager 에 포함시킬 Voter 들을 생성하여 전달
     */
    private List<AccessDecisionVoter<?>> getAccessDecisionVoters(ResourceService resourceService) {
        AuthenticatedVoter authenticatedVoter = new AuthenticatedVoter();           // 인증관련 유권자
        WebExpressionVoter webExpressionVoter = new WebExpressionVoter();           // 웹 승인 결정을 처리하는 유권자
        IpAddressVoter ipAddressVoter = new IpAddressVoter(resourceService);        // 접근 가능 Ip 여부를 처리하는 커스텀 유권자

        List<AccessDecisionVoter<? extends Object>> accessDecisionVoterList = Arrays.asList(ipAddressVoter, authenticatedVoter, webExpressionVoter, roleVoter());
        return accessDecisionVoterList;
    }

    /**
     * 계층 권한을 판단하는 Voter Bean
     */
    @Bean
    public RoleHierarchyVoter roleVoter() {
        RoleHierarchyVoter roleHierarchyVoter = new RoleHierarchyVoter(roleHierarchy());
        roleHierarchyVoter.setRolePrefix("ROLE_");
        return roleHierarchyVoter;
    }

    /**
     * 계층 권한 Bean
     */
    @Bean
    public RoleHierarchyImpl roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        return roleHierarchy;
    }

    /** Authentication failure handler
     * 인증 실패 핸들러 Bean
     */
    @Bean
    public AuthenticationFailureHandler customFailureHandler() {
        return new CustomLoginFailureHandler();
    }

    /** Authentication success handler
     * 인증 성공 핸들러 Bean
     */
    @Bean
    public AuthenticationSuccessHandler customSuccessHandler() {
        return new CustomLoginSuccessHandler(accountService);
    }

    /** Custom AuthenticationProvider
     * 사용자 인증작업을 수행하는 Bean
     */
    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider() {
        return new CustomAuthenticationProvider(passwordEncoder());
    }

    /** Custom AuthenticationProvider registration
     * customAuthenticationProvider 를 Provider 리스트에 등록
     */
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customAuthenticationProvider());
    }

    /** Form login settings
     * 로그인 및 인증절체에대한 세팅을 등록
     */
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .anyRequest().authenticated()
        .and()
                .formLogin()
                .loginPage("/admin/login")
                .loginProcessingUrl("/admin/login/process")
                .usernameParameter("accountId")
                .passwordParameter("password")
                .successHandler(customSuccessHandler())
                .failureHandler(customFailureHandler())
        .and()
                .exceptionHandling()
                .accessDeniedPage("/common/error/404.html")
                .and()
                .logout()
        .and()
                //.addFilterBefore(permitAllFilter(), FilterSecurityInterceptor.class);
                .addFilterBefore(customFilterSecurityInterceptor(), FilterSecurityInterceptor.class);
    }
}
