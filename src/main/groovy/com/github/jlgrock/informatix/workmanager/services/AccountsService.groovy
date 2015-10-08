package com.github.jlgrock.informatix.workmanager.services

import com.github.jlgrock.informatix.workmanager.domain.tokens.PasswordResetToken
import com.github.jlgrock.informatix.workmanager.domain.tokens.PasswordResetTokenRepository
import com.github.jlgrock.informatix.workmanager.domain.tokens.VerificationToken
import com.github.jlgrock.informatix.workmanager.domain.tokens.VerificationTokenRepository
import com.github.jlgrock.informatix.workmanager.domain.useraccount.UserAccount
import com.github.jlgrock.informatix.workmanager.domain.useraccount.UserAccountDTO
import com.github.jlgrock.informatix.workmanager.domain.useraccount.UserAccountRepository
import com.github.jlgrock.informatix.workmanager.domain.useraccount.Role
import com.github.jlgrock.informatix.workmanager.domain.useraccount.UserRoleRepository
import com.github.jlgrock.informatix.workmanager.exceptions.UserException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
/**
 *
 */
@Service
class AccountsService {

    private static Logger LOGGER = LoggerFactory.getLogger(AccountsService.class);

    @Autowired
    UserAccountRepository userAccountRepository

    @Autowired
    UserRoleRepository userRoleRepository

    @Autowired
    VerificationTokenRepository verificationTokenRepository

    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository

    Collection<UserAccount> getAll() {
        userAccountRepository.findAll()
    }

    UserAccount get(int id) {
        userAccountRepository.findOne(id)
    }

    UserAccount add(UserAccountDTO userAccountDTO) {
        if (userAccountRepository.findUserAccountsByEmail(userAccountDTO.email).size() > 0) {
            throw new UserException("This email address has already been used to create an account")
        }
        UserAccount userAccount = new UserAccount(userAccountDTO)
        userAccountRepository.save(userAccount)
    }

    UserAccount update(int id, UserAccountDTO userAccountDTO) {
        if (userAccountRepository.findUserAccountsByEmailAndIdNot(userAccountDTO.email, id).size() > 0) {
            throw new UserException("This email address has already been used to create an account")
        }
        UserAccount currentUser = userAccountRepository.findOne(id)
        if(currentUser.role != userAccountDTO.role &&
                currentUser.role == Role.ADMIN &&
                userAccountRepository.findUserAccountsByRole(Role.ADMIN).size() == 1) {
            def s = "There must always be one ADMIN in the system"
            LOGGER.error(s)
            throw new UserException(s)
        }
        userAccountDTO.id = id
        UserAccount userAccount = new UserAccount(userAccountDTO)
        userAccountRepository.save(userAccount)
    }

    void resetPassword(String email) {
        UserAccount userAccount = userAccountRepository.findOneUserAccountByEmail(email)
        if (userAccount == null) {
            throw new UserException("Email does not exist in the system")
        }
        PasswordResetToken passwordResetToken = new PasswordResetToken(userAccount)
        passwordResetTokenRepository.save(passwordResetToken)
        //TODO send email
    }

    void askForVerification(int id) {
        UserAccount userAccount = userAccountRepository.findOne(id)
        if (userAccount == null) {
            throw new UserException("User does not exist in the system")
        }
        VerificationToken verificationToken = new VerificationToken()
        verificationTokenRepository.save(verificationToken)
        //TODO send email
    }

    UserAccount delete(int id) {
        UserAccount currentUser = userAccountRepository.findOne(id)
        if (currentUser.role == Role.ADMIN &&
                userAccountRepository.findUserAccountsByRole(Role.ADMIN).size() == 1) {
            def s = "There must always be one ADMIN in the system"
            LOGGER.error(s)
            throw new UserException(s)
        }
        userAccountRepository.delete(id)
    }
}
