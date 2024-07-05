package me.silvermail.backend.service;

import me.silvermail.backend.dictionary.Language;
import me.silvermail.backend.entity.Mailbox;
import me.silvermail.backend.entity.User;
import me.silvermail.backend.entity.domain.AbstractUserDomain;
import me.silvermail.backend.entity.domain.CustomDomain;
import me.silvermail.backend.entity.domain.Subdomain;
import me.silvermail.backend.entity.token.UserEmailVerificationToken;
import me.silvermail.backend.entity.token.UserPasswordResetToken;
import me.silvermail.backend.exception.http.AccessDeniedHttpException;
import me.silvermail.backend.repository.UserRepository;
import me.silvermail.backend.service.token.UserEmailVerificationTokenService;
import me.silvermail.backend.service.token.UserPasswordResetTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class UserService implements UserDetailsService {
    public static final List<String> AVAILABLE_LANGUAGE_LIST = List.of(
            Language.LANGUAGE_EN,
            Language.LANGUAGE_RU
    );

    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected PasswordEncoder passwordEncoder;
    @Autowired
    @Lazy
    protected AuthenticationManager authenticationManager;
    @Autowired
    protected MailboxService mailboxService;
    @Autowired
    protected UserPasswordResetTokenService userPasswordResetTokenService;
    @Autowired
    protected UserEmailVerificationTokenService userEmailVerificationTokenService;
    @Autowired
    protected DomainService domainService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        return user;
    }

    public User getUserById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public User createUserWithMailbox(String email, String password, String language) throws Exception {
        User user = createUser(email, password, language);

        mailboxService.createMailbox(user, email);

        return user;
    }

    public User createUser(String email, String password, String language) throws Exception {
        email = email.trim();
        password = password.trim();

        if (!AVAILABLE_LANGUAGE_LIST.contains(language)) {
            throw new Exception("Invalid language");
        }

        User existedUser = getUserByEmail(email);
        if (existedUser != null) {
            throw new IllegalArgumentException("User already exists");
        }

        User user = new User();
        user.setEmail(email);
        user.setEmailVerified(false);
        user.setRole(User.ROLE_USER);
        user.setLanguage(language);
        user.setEmailMode(User.EMAIL_MODE_HARD_REPLACE);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        setUserPassword(user, password);

        userRepository.save(user);

        return user;
    }

    @Transactional
    public void resetUserPasswordByToken(UserPasswordResetToken token, String password) throws Exception {
        updateUserPassword(token.getUser(), password);
        userPasswordResetTokenService.removeToken(token);
    }

    public void updateUserPassword(User user, String newPassword) {
        setUserPassword(user, newPassword);
        saveUser(user);
    }

    public void updateUserLanguage(User user, String language) throws Exception {
        if (!AVAILABLE_LANGUAGE_LIST.contains(language)) {
            throw new Exception("Invalid language");
        }
        user.setLanguage(language);
        saveUser(user);
    }

    public void setUserPassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
    }

    @Transactional
    public void verifyUserEmailByToken(UserEmailVerificationToken token) throws Exception {
        if (!Objects.equals(token.getUser().getEmail(), token.getEmail())) {
            throw new IllegalArgumentException("Invalid token");
        }

        if (token.getUser().isEmailVerified()) {
            throw new IllegalArgumentException("Email is already verified");
        }

        token.getUser().setEmailVerified(true);
        saveUser(token.getUser());

        Mailbox mailbox = mailboxService.getMailboxByUserIdAndEmail(
                token.getUser().getId(),
                token.getEmail()
        );
        if (mailbox != null) {
            mailbox.setEmailVerified(true);
            mailboxService.saveMailbox(mailbox);
        }

        userEmailVerificationTokenService.removeToken(token);
    }

    public void saveUser(User user) {
        user.setUpdateTime(new Date());

        userRepository.save(user);
    }

    @Transactional
    public void removeUser(User user) {
        for (AbstractUserDomain domain : domainService.getUserDomainListByUserId(user.getId())) {
            domainService.removeDomain(domain);
        }

        userRepository.delete(user);
    }
}
