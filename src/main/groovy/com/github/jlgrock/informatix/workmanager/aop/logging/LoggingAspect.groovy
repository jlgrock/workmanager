package com.github.jlgrock.informatix.workmanager.aop.logging
import com.github.jlgrock.informatix.workmanager.config.Constants
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.AfterThrowing
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.env.Environment

import javax.inject.Inject
/**
 * Aspect for logging execution of service and repository Spring components.
 */
@Aspect
class LoggingAspect {

    private final Logger log = LoggerFactory.getLogger(this.getClass())

    @Inject
    private Environment env

    @Pointcut("within(com.github.jlgrock.informatix.workmanager.repository..*) || within(com.github.jlgrock.informatix.workmanager.service..*) || within(com.github.jlgrock.informatix.workmanager.web.rest..*)")
    public void loggingPointcut() {}

    @AfterThrowing(pointcut = "loggingPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        if (env.acceptsProfiles(Constants.SPRING_PROFILE_DEVELOPMENT)) {
            log.error("Exception in {}.{}() with cause = {}", joinPoint.signature.declaringTypeName,
                    joinPoint.signature.name, e.cause, e)
        } else {
            log.error("Exception in {}.{}() with cause = {}", joinPoint.signature.declaringTypeName,
                    joinPoint.signature.name, e.cause)
        }
    }

    @Around("loggingPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        if (log.isDebugEnabled()) {
            log.debug("Enter: {}.{}() with argument[s] = {}", joinPoint.signature.declaringTypeName,
                    joinPoint.signature.name, Arrays.toString(joinPoint.args))
        }
        try {
            Object result = joinPoint.proceed()
            if (log.isDebugEnabled()) {
                log.debug("Exit: {}.{}() with result = {}", joinPoint.signature.declaringTypeName,
                        joinPoint.signature.name, result);
            }
            return result;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.args),
                    joinPoint.signature.declaringTypeName, joinPoint.signature.name);

            throw e
        }
    }
}
