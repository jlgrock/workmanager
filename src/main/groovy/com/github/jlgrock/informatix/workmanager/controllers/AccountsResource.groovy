package com.github.jlgrock.informatix.workmanager.controllers

import com.github.jlgrock.informatix.workmanager.domain.useraccount.UserAccount
import com.github.jlgrock.informatix.workmanager.domain.useraccount.UserAccountDTO
import com.github.jlgrock.informatix.workmanager.exceptions.UserException
import com.github.jlgrock.informatix.workmanager.services.AccountsService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

import javax.validation.Valid

/**
 *
 */
@RestController
@RequestMapping("/accounts")
class AccountsResource extends AbstractSpringResource {

    @Autowired
    AccountsService accountsService

    private static Logger LOGGER = LoggerFactory.getLogger(AccountsResource.class)

    @RequestMapping(method = RequestMethod.GET)
    Collection<UserAccount> get() {
        accountsService.getAll()
    }

    @RequestMapping(method = RequestMethod.GET, value="/{id}")
    UserAccount get(@PathVariable int id) {
        if (id == null) {
            throw new UserException("id is null. Cannot continue with edit")
        }
        accountsService.get(id)
    }

    @RequestMapping(method = RequestMethod.POST, value="/{id}")
    UserAccount post(
            @PathVariable int id,
            @RequestBody @Valid UserAccountDTO userAccountData) {

        if (id == null) {
            throw new UserException("id is null. Cannot continue with edit")
        }

        accountsService.update(id, userAccountData)
    }

    @RequestMapping(method = RequestMethod.PUT)
    UserAccount put(
            @RequestBody @Valid UserAccountDTO userAccountData) {
        LOGGER.debug("userAccountData: ${userAccountData}")
        if (userAccountData == null) {
            throw new UserException("userAccountData is null. Cannot continue with add")
        }
        accountsService.add(userAccountData)
    }

    @RequestMapping(method = RequestMethod.DELETE, value="/{id}")
    UserAccount delete(@PathVariable("id") int id) {
        if (id == null) {
            throw new UserException("id is null. Cannot continue with delete")
        }
        accountsService.delete(id)
    }

    @RequestMapping(method = RequestMethod.GET, value="/reset")
    UserAccount resetPassword(@RequestParam String email) {
        if (email == null) {
            throw new UserException("id is null. Cannot continue with password reset")
        }

        accountsService.resetPassword(email)
    }

    @RequestMapping(method = RequestMethod.GET, value="/{id}/validate")
    UserAccount validateUser(@PathVariable int id, @RequestParam token) {
        if (id == null) {
            throw new UserException("id is null. Cannot continue with edit")
        }
        if (token == null) {
            throw new UserException("id is null. Cannot continue with edit")
        }

        accountsService.askForVerification(id)
    }

}
