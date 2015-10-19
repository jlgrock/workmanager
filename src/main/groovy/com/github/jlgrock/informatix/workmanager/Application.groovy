package com.github.jlgrock.informatix.workmanager
import com.github.jlgrock.informatix.workmanager.config.Constants
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.SpringApplication
import org.springframework.boot.actuate.autoconfigure.MetricFilterAutoConfiguration
import org.springframework.boot.actuate.autoconfigure.MetricRepositoryAutoConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.core.env.Environment
import org.springframework.core.env.SimpleCommandLinePropertySource

import javax.annotation.PostConstruct
import javax.inject.Inject

@ComponentScan
@EnableAutoConfiguration(exclude = [MetricFilterAutoConfiguration.class, MetricRepositoryAutoConfiguration.class])
class Application {

    static final Logger log = LoggerFactory.getLogger(Application.class)

    @Inject
    Environment env

    /**
     * Initializes workmanager.
     * <p/>
     * Spring profiles can be configured with a program arguments --spring.profiles.active=your-active-profile
     * <p/>
     * <p>
     * Currently set up with three Spring Profiles:

     * "dev" for development: The default profile.  it focuses on ease of development and productivity
     * "prod" for production: it focuses on performance and scalability
     * "fast" for advanced users in development: it focuses on fast start-up time, at the cost of disabling some services

     If you run the application in your IDE, run the "Application" class.  If you run via Gradle, run `gradle bootRun`

     To run with the production profile, run the following:

     `gradle bootRun prod`

     The "fast" profile does the following:
     * automatically uses the settings from the "dev" profile
     * Uses Undertow instead of Tomcat
     * Disables Liquibase (so you should use this profile with an external database, and not H2, or your database will be empty)
     * Disables Swagger (if you have lots of REST endpoints, Swagger will become very slow)
     * Disables the Async service
     * Disables the Cache service
     * Disables the Metrics service

     To use the "fast" profile with the "dev" profile, run the following:

     `gradle bootRun fast`

     * </p>
     */
    @PostConstruct
    void initApplication() throws IOException {
        if (env.getActiveProfiles().length == 0) {
            log.warn("No Spring profile configured, running with default configuration")
        } else {
            log.info("Running with Spring profile(s) : {}", Arrays.toString(env.getActiveProfiles()))
            Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles())
            if (activeProfiles.contains(Constants.SPRING_PROFILE_DEVELOPMENT) && activeProfiles.contains(Constants.SPRING_PROFILE_PRODUCTION)) {
                log.error("You have misconfigured your application! " +
                    "It should not run with both the 'dev' and 'prod' profiles at the same time.")
            }
            if (activeProfiles.contains(Constants.SPRING_PROFILE_PRODUCTION) && activeProfiles.contains(Constants.SPRING_PROFILE_FAST)) {
                log.error("You have misconfigured your application! " +
                    "It should not run with both the 'prod' and 'fast' profiles at the same time.")
            }
            if (activeProfiles.contains(Constants.SPRING_PROFILE_DEVELOPMENT) && activeProfiles.contains(Constants.SPRING_PROFILE_CLOUD)) {
                log.error("You have misconfigured your application! " +
                    "It should not run with both the 'dev' and 'cloud' profiles at the same time.")
            }
        }
    }

    /**
     * Main method, used to run the application.
     */
    static void main(String[] args) throws UnknownHostException {
        SpringApplication app = new SpringApplication(Application.class)
        app.setShowBanner(false)
        SimpleCommandLinePropertySource source = new SimpleCommandLinePropertySource(args)
        addDefaultProfile(app, source)
        addLiquibaseScanPackages()
        Environment env = app.run(args).getEnvironment()
        log.info("Access URLs:\n----------------------------------------------------------\n\t" +
            "Local: \t\thttp://127.0.0.1:{}\n\t" +
            "External: \thttp://{}:{}\n----------------------------------------------------------",
            env.getProperty("server.port"),
            InetAddress.getLocalHost().getHostAddress(),
            env.getProperty("server.port"))

    }

    /**
     * If no profile has been configured, set by default the "dev" profile.
     */
    private static void addDefaultProfile(SpringApplication app, SimpleCommandLinePropertySource source) {
        if (!source.containsProperty("spring.profiles.active") &&
                !System.getenv().containsKey("SPRING_PROFILES_ACTIVE")) {

            app.setAdditionalProfiles(Constants.SPRING_PROFILE_DEVELOPMENT)
        }
    }

    /**
     * Set the liquibases.scan.packages to avoid an exception from ServiceLocator.
     */
    private static void addLiquibaseScanPackages() {
        System.setProperty("liquibase.scan.packages", "liquibase.change,liquibase.database," +
            "liquibase.parser,liquibase.precondition,liquibase.datatype," +
            "liquibase.serializer,liquibase.sqlgenerator,liquibase.executor," +
            "liquibase.snapshot,liquibase.logging,liquibase.diff," +
            "liquibase.structure,liquibase.structurecompare,liquibase.lockservice," +
            "liquibase.ext,liquibase.changelog")
    }
}
