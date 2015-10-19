package com.github.jlgrock.informatix.workmanager.config

import com.github.jlgrock.informatix.workmanager.config.locale.AngularCookieLocaleResolver
import org.springframework.boot.bind.RelaxedPropertyResolver
import org.springframework.context.EnvironmentAware
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import org.springframework.core.env.Environment
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor

@Configuration
class LocaleConfiguration extends WebMvcConfigurerAdapter implements EnvironmentAware {

    RelaxedPropertyResolver propertyResolver

    @Override
    void setEnvironment(Environment environment) {
        this.propertyResolver = new RelaxedPropertyResolver(environment, "spring.messageSource.")
    }

    @Bean(name = "localeResolver")
    LocaleResolver localeResolver() {
        AngularCookieLocaleResolver cookieLocaleResolver = new AngularCookieLocaleResolver()
        cookieLocaleResolver.setCookieName("NG_TRANSLATE_LANG_KEY")
        return cookieLocaleResolver
    }

    @Bean
    MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource()
        messageSource.setBasename("classpath:/i18n/messages")
        messageSource.setDefaultEncoding("UTF-8")
        messageSource.setCacheSeconds(propertyResolver.getProperty("cacheSeconds", Integer.class, 1))
        return messageSource
    }

    @Override
    void addInterceptors(InterceptorRegistry registry) {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor()
        localeChangeInterceptor.setParamName("language")
        registry.addInterceptor(localeChangeInterceptor)
    }
}
