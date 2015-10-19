package com.github.jlgrock.informatix.workmanager.web.rest

import com.codahale.metrics.annotation.Timed
import com.github.jlgrock.informatix.workmanager.domain.tokens.PersistentToken
import com.github.jlgrock.informatix.workmanager.domain.User
import com.github.jlgrock.informatix.workmanager.repository.PersistentTokenRepository
import com.github.jlgrock.informatix.workmanager.repository.UserRepository
import com.github.jlgrock.informatix.workmanager.security.SecurityUtils
import com.github.jlgrock.informatix.workmanager.service.MailService
import com.github.jlgrock.informatix.workmanager.service.UserService
import com.github.jlgrock.informatix.workmanager.web.rest.dto.KeyAndPasswordDTO
import com.github.jlgrock.informatix.workmanager.web.rest.dto.UserDTO
import org.apache.commons.lang.StringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

import javax.inject.Inject
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid
import java.util.*

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
public class AccountResource extends AbstractSpringResource {

    final Logger log = LoggerFactory.getLogger(AccountResource.class)

    @Inject
    UserRepository userRepository

    @Inject
    UserService userService

    @Inject
    PersistentTokenRepository persistentTokenRepository

    @Inject
    MailService mailService

    /**
     * POST  /register -> register the user.
     */
    @RequestMapping(value = "/register",
            method = RequestMethod.POST,
            produces = MediaType.TEXT_PLAIN_VALUE)
    @Timed
    public ResponseEntity<?> registerAccount(@Valid @RequestBody UserDTO userDTO, HttpServletRequest request) {
        Optional<User> user = userRepository.findOneByLogin(userDTO.login)
        if (user.present) {
            return new ResponseEntity<>("login already in use", HttpStatus.BAD_REQUEST)
        }
        user = userRepository.findOneByEmail(userDTO.email)
        if (user.present) {
            new ResponseEntity<>("e-mail address already in use", HttpStatus.BAD_REQUEST)
        }
        User newUser = userService.createUserInformation(userDTO.login, userDTO.password,
                    userDTO.firstName, userDTO.lastName, userDTO.email.toLowerCase(), userDTO.langKey)
        String baseUrl = "${request.scheme}://${request.serverName}:${request.serverPort}"
        mailService.sendActivationEmail(newUser, baseUrl)
        return new ResponseEntity<>(HttpStatus.CREATED)
    }

    /**
     * GET  /activate -> activate the registered user.
     */
    @RequestMapping(value = "/activate",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<String> activateAccount(@RequestParam(value = "key") String key) {
        Optional<User> user = userService.activateRegistration(key)
        if (user.present) {
            new ResponseEntity<String>(HttpStatus.OK)
        }
        new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR)
    }

    /**
     * GET  /authenticate -> check if the user is authenticated, and return its login.
     */
    @RequestMapping(value = "/authenticate",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public String isAuthenticated(HttpServletRequest request) {
        log.debug("REST request to check if the current user is authenticated")
        return request.getRemoteUser()
    }

    /**
     * GET  /account -> get the current user.
     */
    @RequestMapping(value = "/account",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserDTO> getAccount() {
        User user = userService.getUserWithAuthorities()
        if (user != null) {
            return new ResponseEntity<>(new UserDTO(user), HttpStatus.OK)
        }
        new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR)
    }

    /**
     * POST  /account -> update the current user information.
     */
    @RequestMapping(value = "/account",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<String> saveAccount(@RequestBody UserDTO userDTO) {
        Optional<User> user = userRepository.findOneByLogin(userDTO.login)
        if (user.present && user.get().login.equals(SecurityUtils.currentLogin)) {
            userService.updateUserInformation(userDTO.firstName, userDTO.lastName, userDTO.email, userDTO.langKey)
            return new ResponseEntity<String>(HttpStatus.OK)
        }
        new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR)
    }

    /**
     * POST  /change_password -> changes the current user's password
     */
    @RequestMapping(value = "/account/change_password",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> changePassword(@RequestBody String password) {
        if (!checkPasswordLength(password)) {
            return new ResponseEntity<>("Incorrect password", HttpStatus.BAD_REQUEST)
        }
        userService.changePassword(password)
        return new ResponseEntity<>(HttpStatus.OK)
    }

    /**
     * GET  /account/sessions -> get the current open sessions.
     */
    @RequestMapping(value = "/account/sessions",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<PersistentToken>> getCurrentSessions() {
        Optional<User> user = userRepository.findOneByLogin(SecurityUtils.currentLogin)
       if (user.present) {
           return new ResponseEntity<>(persistentTokenRepository.findByUser(user.get()), HttpStatus.OK)
       }
       new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR)
    }

    /**
     * DELETE  /account/sessions?series={series} -> invalidate an existing session.
     *
     * - You can only delete your own sessions, not any other user's session
     * - If you delete one of your existing sessions, and that you are currently logged in on that session, you will
     *   still be able to use that session, until you quit your browser: it does not work in real time (there is
     *   no API for that), it only removes the "remember me" cookie
     * - This is also true if you invalidate your current session: you will still be able to use it until you close
     *   your browser or that the session times out. But automatic login (the "remember me" cookie) will not work
     *   anymore.
     *   There is an API to invalidate the current session, but there is no API to check which session uses which
     *   cookie.
     */
    @RequestMapping(value = "/account/sessions/{series}",
            method = RequestMethod.DELETE)
    @Timed
    public void invalidateSession(@PathVariable String series) throws UnsupportedEncodingException {
        String decodedSeries = URLDecoder.decode(series, "UTF-8")
        Optional<User> user = userRepository.findOneByLogin(SecurityUtils.currentLogin)
        if (user.present) {
            List<PersistentToken> persistentTokens = persistentTokenRepository.findByUser(user.get())
            persistentTokens.each {
                persistentToken ->
                if (StringUtils.equals(persistentToken.series, decodedSeries)) {
                    persistentTokenRepository.delete(decodedSeries)
                }
            }

        }
    }

    @RequestMapping(value = "/account/reset_password/init",
        method = RequestMethod.POST,
        produces = MediaType.TEXT_PLAIN_VALUE)
    @Timed
    public ResponseEntity<?> requestPasswordReset(@RequestBody String mail, HttpServletRequest request) {
        Optional<User> user = userService.requestPasswordReset(mail)
        if (user.present) {
            String baseUrl = "${request.getScheme()}://${request.serverName}:request.serverPort"
            mailService.sendPasswordResetMail(user, baseUrl)
            return new ResponseEntity<>("e-mail was sent", HttpStatus.OK)
        }

        new ResponseEntity<>("e-mail address not registered", HttpStatus.BAD_REQUEST)
    }

    @RequestMapping(value = "/account/reset_password/finish",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<String> finishPasswordReset(@RequestBody KeyAndPasswordDTO keyAndPassword) {
        if (!checkPasswordLength(keyAndPassword.newPassword)) {
            return new ResponseEntity<>("Incorrect password", HttpStatus.BAD_REQUEST)
        }
        Optional<User> passwordReset = userService.completePasswordReset(keyAndPassword.newPassword, keyAndPassword.key)
        if (passwordReset.present) {
            return new ResponseEntity<String>(HttpStatus.OK)
        }
        new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR)
    }

    private boolean checkPasswordLength(String password) {
      return (!StringUtils.isEmpty(password) && password.length() >= UserDTO.PASSWORD_MIN_LENGTH && password.length() <= UserDTO.PASSWORD_MAX_LENGTH)
    }

}
