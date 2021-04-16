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
    private final String[] permitAllPattern = {"/admin/login","/admin/login/process","/admin/css/**","/admin/js/**","/admin/img/**","/oauth2/**"};

    // PasswordEncoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Custom FilterSecurityInterceptor
    @Bean
    public PermitAllFilter permitAllFilter() throws Exception {
        PermitAllFilter permitAllFilter = new PermitAllFilter(permitAllPattern);
        permitAllFilter.setAccessDecisionManager(affirmativeBased(resourceService));
        permitAllFilter.setSecurityMetadataSource(urlSecurityMetadataSource(resourceService));
        permitAllFilter.setRejectPublicInvocations(false);

        return permitAllFilter;
    }

    // Authorization processing object
    @Bean
    public FilterInvocationSecurityMetadataSource urlSecurityMetadataSource(ResourceService resourceService) {
        return new UrlSecurityMetadataSource(urlResourcesMapFactoryBean(resourceService).getObject(),resourceService);
    }

    // UrlResourcesMapFactoryBean
    @Bean
    public UrlResourcesMapFactoryBean urlResourcesMapFactoryBean(ResourceService resourceService){
        UrlResourcesMapFactoryBean urlResourcesMapFactoryBean = new UrlResourcesMapFactoryBean();
        urlResourcesMapFactoryBean.setResourcesService(resourceService);
        return urlResourcesMapFactoryBean;
    }

    // AccessDecisionManager
    private AccessDecisionManager affirmativeBased(ResourceService resourceService) {
        AffirmativeBased accessDecisionManager = new AffirmativeBased(getAccessDecisionVoters(resourceService));
        accessDecisionManager.setAllowIfAllAbstainDecisions(false);
        return accessDecisionManager;
    }

    // AccessDecisionVoters
    private List<AccessDecisionVoter<?>> getAccessDecisionVoters(ResourceService resourceService) {
        AuthenticatedVoter authenticatedVoter = new AuthenticatedVoter();
        WebExpressionVoter webExpressionVoter = new WebExpressionVoter();
        IpAddressVoter ipAddressVoter = new IpAddressVoter(resourceService);

        List<AccessDecisionVoter<? extends Object>> accessDecisionVoterList = Arrays.asList(ipAddressVoter, authenticatedVoter, webExpressionVoter, roleVoter());
        return accessDecisionVoterList;
    }

    // Authentication failure handler
    @Bean
    public AuthenticationFailureHandler customFailureHandler() {
        return new CustomLoginFailureHandler();
    }

    // Authentication success handler
    @Bean
    public AuthenticationSuccessHandler customSuccessHandler() {
        return new CustomLoginSuccessHandler(accountService);
    }

    // Custom AuthenticationProvider
    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider() {
        return new CustomAuthenticationProvider(passwordEncoder());
    }

    @Bean
    public RoleHierarchyVoter roleVoter() {
        RoleHierarchyVoter roleHierarchyVoter = new RoleHierarchyVoter(roleHierarchy());
        roleHierarchyVoter.setRolePrefix("ROLE_");
        return roleHierarchyVoter;
    }

    @Bean
    public RoleHierarchyImpl roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        return roleHierarchy;
    }

    // Custom AuthenticationProvider registration
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customAuthenticationProvider());
    }

    // Form login settings
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .anyRequest().authenticated()
        .and()
                .formLogin()
                .loginPage("/admin/login")
                .loginProcessingUrl("/admin/login/process")
                .usernameParameter("accounId")
                .passwordParameter("password")
                .successHandler(customSuccessHandler())
                .failureHandler(customFailureHandler())
        .and()
                .exceptionHandling()
                .accessDeniedPage("/common/error/404.html")
                .and()
                .logout()
        .and()
                .addFilterBefore(permitAllFilter(), FilterSecurityInterceptor.class);
    }
}
