package com.github.jlgrock.informatix.workmanager.config.apidoc

import com.github.jlgrock.informatix.workmanager.config.Constants
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.bind.RelaxedPropertyResolver
import org.springframework.context.EnvironmentAware
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.env.Environment
import org.springframework.http.ResponseEntity
import org.springframework.util.StopWatch
import springfox.documentation.service.ApiInfo
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime

import static springfox.documentation.builders.PathSelectors.regex
/**
 * Springfox Swagger configuration.
 *
 * Warning! When having a lot of REST endpoints, Springfox can become a performance issue. In that
 * case, you can use a specific Spring profile for this class, so that only front-end developers
 * have access to the Swagger view.
 */
@Configuration
@EnableSwagger2
@Profile(Constants.NOT_SPRING_PROFILE_PRODUCTION)
class SwaggerConfiguration implements EnvironmentAware {

    final Logger log = LoggerFactory.getLogger(SwaggerConfiguration.class)

    public static final String DEFAULT_INCLUDE_PATTERN = "/api/.*"

    RelaxedPropertyResolver propertyResolver

    @Override
    void setEnvironment(Environment environment) {
        this.propertyResolver = new RelaxedPropertyResolver(environment, "swagger.")
    }

    /**
     * Swagger Springfox configuration.
     */
    @Bean
    Docket swaggerSpringfoxDocket() {
        log.debug("Starting Swagger")
        StopWatch watch = new StopWatch()
        watch.start()
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            .genericModelSubstitutes(ResponseEntity.class)
            .forCodeGeneration(true)
            .genericModelSubstitutes(ResponseEntity.class)
            .directModelSubstitute(LocalDate.class, String.class)
            .directModelSubstitute(ZonedDateTime.class, Date.class)
            .directModelSubstitute(LocalDateTime.class, Date.class)
            .select()
            .paths(regex(DEFAULT_INCLUDE_PATTERN))
            .build()
        watch.stop()
        log.debug("Started Swagger in {} ms", watch.getTotalTimeMillis())
        docket
    }

    /**
     * API Info as it appears on the swagger-ui page.
     */
    private ApiInfo apiInfo() {
        new ApiInfo(
            propertyResolver.getProperty("title"),
            propertyResolver.getProperty("description"),
            propertyResolver.getProperty("version"),
            propertyResolver.getProperty("termsOfServiceUrl"),
            propertyResolver.getProperty("contact"),
            propertyResolver.getProperty("license"),
            propertyResolver.getProperty("licenseUrl"))
    }
}
