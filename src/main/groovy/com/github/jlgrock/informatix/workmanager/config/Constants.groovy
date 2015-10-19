package com.github.jlgrock.informatix.workmanager.config

/**
 * Application constants.
 */
final class Constants {

    // Spring profile for development, production and "fast"
    public static final String SPRING_PROFILE_DEVELOPMENT = "dev"
    public static final String SPRING_PROFILE_PRODUCTION = "prod"
    public static final String SPRING_PROFILE_FAST = "fast"
    // Spring profile used when deploying with Spring Cloud (used when deploying to CloudFoundry)
    public static final String SPRING_PROFILE_CLOUD = "cloud"
    // Spring profile used when deploying to Heroku
    public static final String SPRING_PROFILE_HEROKU = "heroku"

    public static final String SYSTEM_ACCOUNT = "system"

    public static final String NOT_SPRING_PROFILE_DEVELOPMENT = "!dev";
    public static final String NOT_SPRING_PROFILE_PRODUCTION = "!prod";
    public static final String NOT_SPRING_PROFILE_FAST = "!fast";
    // Spring profile used when deploying with Spring Cloud (used when deploying to CloudFoundry)
    public static final String NOT_SPRING_PROFILE_CLOUD = "!cloud";

    private Constants() {
    }
}
