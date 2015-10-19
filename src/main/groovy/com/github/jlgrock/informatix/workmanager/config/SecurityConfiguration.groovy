package com.github.jlgrock.informatix.workmanager.config

import com.github.jlgrock.informatix.workmanager.security.AjaxAuthenticationFailureHandler
import com.github.jlgrock.informatix.workmanager.security.AjaxAuthenticationSuccessHandler
import com.github.jlgrock.informatix.workmanager.security.AjaxLogoutSuccessHandler
import com.github.jlgrock.informatix.workmanager.security.AuthoritiesConstants
import com.github.jlgrock.informatix.workmanager.security.Http401UnauthorizedEntryPoint
import com.github.jlgrock.informatix.workmanager.web.filter.CsrfCookieGeneratorFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension
import org.springframework.security.web.authentication.RememberMeServices
import org.springframework.security.web.csrf.CsrfFilter

import javax.inject.Inject

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Inject
    Environment env

    @Inject
    AjaxAuthenticationSuccessHandler ajaxAuthenticationSuccessHandler

    @Inject
    AjaxAuthenticationFailureHandler ajaxAuthenticationFailureHandler

    @Inject
    AjaxLogoutSuccessHandler ajaxLogoutSuccessHandler

    @Inject
    Http401UnauthorizedEntryPoint authenticationEntryPoint

    @Inject
    UserDetailsService userDetailsService

    @Inject
    RememberMeServices rememberMeServices

    @Bean
    PasswordEncoder passwordEncoder() {
        new BCryptPasswordEncoder()
    }

    @Inject
    void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder())
    }

    @Override
    void configure(WebSecurity web) throws Exception {
        web.ignoring()
            .antMatchers("/scripts/**/*.{js,html}")
            .antMatchers("/bower_components/**")
            .antMatchers("/i18n/**")
            .antMatchers("/assets/**")
            .antMatchers("/swagger-ui/index.html")
            .antMatchers("/test/**")
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf()
            .ignoringAntMatchers("/websocket/**")
        .and()
            .addFilterAfter(new CsrfCookieGeneratorFilter(), CsrfFilter.class)
            .exceptionHandling()
            .authenticationEntryPoint(authenticationEntryPoint)
        .and()
            .rememberMe()
            .rememberMeServices(rememberMeServices)
            .rememberMeParameter("remember-me")
            .key(env.getProperty("custom.security.rememberme.key"))
        .and()
            .formLogin()
            .loginProcessingUrl("/api/authentication")
            .successHandler(ajaxAuthenticationSuccessHandler)
            .failureHandler(ajaxAuthenticationFailureHandler)
            .usernameParameter("j_username")
            .passwordParameter("j_password")
            .permitAll()
        .and()
            .logout()
            .logoutUrl("/api/logout")
            .logoutSuccessHandler(ajaxLogoutSuccessHandler)
            .deleteCookies("JSESSIONID")
            .permitAll()
        .and()
            .headers()
            .frameOptions()
            .disable()
        .and()
            .authorizeRequests()
            .antMatchers("/api/register").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/api/activate").permitAll()
            .antMatchers("/api/authenticate").permitAll()
            .antMatchers("/api/account/reset_password/init").permitAll()
            .antMatchers("/api/account/reset_password/finish").permitAll()
            .antMatchers("/api/logs/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/api/audits/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/api/**").authenticated()
            .antMatchers("/metrics/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/health/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/trace/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/dump/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/shutdown/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/beans/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/configprops/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/info/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/autoconfig/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/env/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/trace/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/mappings/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/v2/api-docs/**").permitAll()
            .antMatchers("/configuration/security").permitAll()
            .antMatchers("/configuration/ui").permitAll()
            .antMatchers("/swagger-ui/index.html").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/protected/**").authenticated()

    }

    @Bean
    SecurityEvaluationContextExtension securityEvaluationContextExtension() {
        new SecurityEvaluationContextExtension()
    }
}
