package com.github.jlgrock.informatix.workmanager
import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.databind.module.SimpleModule
import com.github.jlgrock.informatix.workmanager.controllers.LocalDateTimeDeserializer
import com.github.jlgrock.informatix.workmanager.controllers.LocalDateTimeSerializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.SecurityProperties
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.context.web.SpringBootServletInitializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler
import org.springframework.security.web.csrf.CsrfFilter
import org.springframework.security.web.csrf.CsrfToken
import org.springframework.security.web.csrf.CsrfTokenRepository
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.WebUtils

import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.sql.DataSource
import java.security.Principal
import java.time.LocalDateTime
/**
 *
 */
// Normally you would add @EnableWebMvc for a Spring MVC app, but Spring Boot adds it automatically
// when it sees spring-webmvc on the classpath.
@SpringBootApplication // is the same as @Configuration, @EnableConfiguration, and @ComponentScan
@EnableConfigurationProperties // Looks for classes annotated with @ConfigurationProperties and populates them
@RestController
class ApplicationConfiguration extends SpringBootServletInitializer {
    static final Logger LOGGER = LoggerFactory.getLogger(ApplicationConfiguration.class);

    @RequestMapping("/principal")
    Principal user(Principal user) {
        return user;
    }

    // define Jackson serializer for LocalDateTime for REST services
    @Bean
    Module handleLocalDateTime() {
        SimpleModule module = new SimpleModule("EnhancedLocalDateTimeModule", new Version(0,1,0, "alpha"))
        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer())
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer())
        module
    }

    @Autowired
    DataSource dataSource;

    static BCryptPasswordEncoder BCRYPT_ENCODER = new BCryptPasswordEncoder();

    @Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery(
                "select email as principle, password as credentials, true as enabled from user_accounts where email=?")
                .authoritiesByUsernameQuery(
                "select u.email as principle, r.role from user_accounts u, user_roles r where u.id = r.user_account_id and u.email=?")
                .passwordEncoder(BCRYPT_ENCODER);
    }

    @Configuration
    @Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
    @EnableWebSecurity
    protected static class SecurityConfiguration extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {

            http.httpBasic();

            http.authorizeRequests()
                    // the following paths are just passed through
                    .antMatchers("/", "/favicon.ico", "/index.html", "/**/*.js", "/**/*.css", "/templates/*.html").permitAll()
                    // the following path requires you to be an admin user
                    .antMatchers("/admin/**").hasRole("ADMIN")
                    // all other requires authentication
                    .anyRequest().authenticated()
//                .and()
//                    .authorizeRequests()
//                        .antMatchers("/admin/**")
//                        .hasRole("ADMIN")
//                        .anyRequest()
//                        .authenticated()
            // use the
            http.csrf().csrfTokenRepository(csrfTokenRepository())
                    .and().addFilterAfter(csrfHeaderFilter(), CsrfFilter.class)

            //disable csrf
            //http.csrf.disable()

            // where the redirect sends you, override the success handler so that it doesn't forward you at all
            http.logout()
                .permitAll()
                .logoutUrl("/logout")
                .logoutSuccessHandler(
                    new LogoutSuccessHandler() {
                        @Override
                        void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                            // Do nothing
                        }
                    }
                )

            //http.exceptionHandling().accessDeniedPage("/403")

        }

        private Filter csrfHeaderFilter() {
            return new OncePerRequestFilter() {
                @Override
                protected void doFilterInternal(HttpServletRequest request,
                                                HttpServletResponse response, FilterChain filterChain)
                        throws ServletException, IOException {

                    // The CSRF Token needs to be renamed to XSRF for Angular to use it
                    CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
                    if (csrf != null) {
                        Cookie cookie = WebUtils.getCookie(request, "XSRF-TOKEN");
                        String token = csrf.getToken();
                        if (cookie == null || token != null
                                && !token.equals(cookie.getValue())) {
                            cookie = new Cookie("XSRF-TOKEN", token);
                            cookie.setPath("/");
                            response.addCookie(cookie);
                        }
                    }
                    filterChain.doFilter(request, response);
                }
            };
        }

        private CsrfTokenRepository csrfTokenRepository() {
            HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
            repository.setHeaderName("X-XSRF-TOKEN");
            return repository;
        }
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ApplicationConfiguration.class);
    }

    static void main(String[] args) throws Exception {
        LOGGER.info("Starting Spring Boot Application.");
        SpringApplication.run(ApplicationConfiguration.class, args);
    }

}
