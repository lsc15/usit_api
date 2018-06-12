package com.usit.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.usit.app.spring.security.authentication.RestAuthenticationEntryPoint;
import com.usit.app.spring.security.authentication.SkipPathRequestMatcher;
import com.usit.app.spring.security.extend.BaseAuthenticationProvider;
import com.usit.app.spring.security.extend.BasePasswordValidator;
import com.usit.app.spring.security.extend.TokenAuthenticationProvider;
import com.usit.app.spring.security.filter.BaseAuthenticationFilter;
import com.usit.app.spring.security.filter.TokenAuthenticationProcessingFilter;
import com.usit.app.spring.security.handler.BaseAuthenticationFailureHandler;
import com.usit.app.spring.security.handler.BaseAuthenticationSuccessHandler;
import com.usit.app.spring.security.handler.TokenAuthenticationFailureHandler;
import com.usit.app.spring.security.handler.TokenAuthenticationSuccessHandler;
import com.usit.app.spring.security.service.UserDetailsService;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String JWT_TOKEN_HEADER_PARAM = "usit-auth";
//    public static final String[] FORM_BASED_LOGIN_ENTRY_POINT = new String[] {"/users/sign-in", "/users", "/contents/**", "/users/email/**","/users/send-verify-email","/users/confirm-email-code","/orders/confirm","/verify-mobile","/confirm-mobile-code"};
    public static final String[] REQUESET_ALLOWED_ENTRY_POINT = new String[] {"/**"};
    public static final String[] TOKEN_BASED_AUTH_ENTRY_POINT = new String[] {"/**"};


    @Autowired
    private UserDetailsService userDetailService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RestAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private FilterInvocationSecurityMetadataSource filterInvocationSecurityMetadataSource;

    public BaseAuthenticationFilter buildBaseAuthenticationFilter() {
        BaseAuthenticationFilter baseAuthenticationFilter = new BaseAuthenticationFilter(userDetailService);
        baseAuthenticationFilter.setFilterProcessesUrl("/users/sign-in");
        baseAuthenticationFilter.setAuthenticationManager(authenticationManager);
        BaseAuthenticationSuccessHandler authenticationSuccessHandler = new BaseAuthenticationSuccessHandler();
        BaseAuthenticationFailureHandler authenticationFailureHandler = new BaseAuthenticationFailureHandler();
        baseAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        baseAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);

        return baseAuthenticationFilter;
    }

    protected TokenAuthenticationProcessingFilter buildTokenAuthenticationProcessingFilter() throws Exception {

        List<String> pathsToSkip = Arrays.asList(REQUESET_ALLOWED_ENTRY_POINT);
        List<String> pathsToProcess = Arrays.asList(TOKEN_BASED_AUTH_ENTRY_POINT);
        SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(pathsToSkip, pathsToProcess);
        TokenAuthenticationProcessingFilter filter = new TokenAuthenticationProcessingFilter(userDetailService, matcher);
        filter.setAuthenticationManager(authenticationManager);
        TokenAuthenticationSuccessHandler authenticationSuccessHandler = new TokenAuthenticationSuccessHandler();
        TokenAuthenticationFailureHandler authenticationFailureHandler = new TokenAuthenticationFailureHandler();
        filter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        filter.setAuthenticationFailureHandler(authenticationFailureHandler);
        return filter;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
        .authenticationProvider(buildBaseAuthenticationProvider())
        .authenticationProvider(buildTokenAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
        .exceptionHandling()
        .authenticationEntryPoint(authenticationEntryPoint)
        .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
            .authorizeRequests().antMatchers("/**").permitAll()
        .and()
        .addFilterBefore(buildBaseAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(buildTokenAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
        .addFilterAfter(filterSecurityInterceptor(), FilterSecurityInterceptor.class);


    }

    @Bean
    public BaseAuthenticationProvider buildBaseAuthenticationProvider() throws Exception {
        BaseAuthenticationProvider authenticationProvider = new BaseAuthenticationProvider();
        authenticationProvider.setUserDetailService(userDetailService);
        authenticationProvider.setSaltSource(saltSource());
        authenticationProvider.setPasswordValidator(basePasswordValidator());
        authenticationProvider.afterPropertiesSet();
        return authenticationProvider;
    }

    @Bean
    public TokenAuthenticationProvider buildTokenAuthenticationProvider() throws Exception {
        TokenAuthenticationProvider authenticationProvider = new TokenAuthenticationProvider();
        authenticationProvider.setUserDetailService(userDetailService);
        authenticationProvider.afterPropertiesSet();
        return authenticationProvider;
    }

    @Bean
    public FilterSecurityInterceptor filterSecurityInterceptor() throws Exception {

        FilterSecurityInterceptor filterSecurityInterceptor = new FilterSecurityInterceptor();
        filterSecurityInterceptor.setAuthenticationManager(authenticationManager);
        filterSecurityInterceptor.setSecurityMetadataSource(filterInvocationSecurityMetadataSource);
        filterSecurityInterceptor.setAccessDecisionManager(affirmativeBased());

        return filterSecurityInterceptor;
    }

    @Bean
    public AffirmativeBased affirmativeBased() {
      List<AccessDecisionVoter<? extends Object>> accessDecisionVoters = new ArrayList<>();
      accessDecisionVoters.add(roleVoter());
      accessDecisionVoters.add(roleVoter2());
      AffirmativeBased affirmativeBased = new AffirmativeBased(accessDecisionVoters);
      return affirmativeBased;
    }

    @Bean
    public RoleVoter roleVoter() {
        RoleVoter roleHierarchyVoter = new RoleVoter();
        roleHierarchyVoter.setRolePrefix("");

        return roleHierarchyVoter;
    }
    @Bean
    public RoleHierarchyVoter roleVoter2() {
      RoleHierarchyVoter roleHierarchyVoter = new RoleHierarchyVoter(roleHierarchy());
      roleHierarchyVoter.setRolePrefix("");
      return roleHierarchyVoter;
    }

    //RoleHierarchy 설정
    @Bean
    public RoleHierarchy roleHierarchy() {
      RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
      roleHierarchy.setHierarchy("USIT_ADMIN > USIT_USER \n USIT_USER > ROLE_ANONYMOUS \n USIT_PARTNER > ROLE_ANONYMOUS");
      return roleHierarchy;
    }

    @Bean
    public SaltSource saltSource() throws Exception {
        ReflectionSaltSource saltSource = new ReflectionSaltSource();
        saltSource.setUserPropertyToUse("salt");
        saltSource.afterPropertiesSet();
        return saltSource;
    }

    @Bean
    public BasePasswordValidator basePasswordValidator() {
        BasePasswordValidator basePasswordValidator = new BasePasswordValidator();
        basePasswordValidator.setPasswordEncoder(new BCryptPasswordEncoder());
        return basePasswordValidator;
    }

}
