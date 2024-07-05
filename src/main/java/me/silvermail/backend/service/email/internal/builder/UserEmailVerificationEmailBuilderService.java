package me.silvermail.backend.service.email.internal.builder;

import me.silvermail.backend.model.email.internal.AbstractInternalEmail;
import me.silvermail.backend.model.email.internal.UserEmailVerificationEmail;
import me.silvermail.backend.entity.User;
import me.silvermail.backend.entity.token.UserEmailVerificationToken;
import me.silvermail.backend.service.UserService;
import me.silvermail.backend.service.token.UserEmailVerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

@Service
public class UserEmailVerificationEmailBuilderService extends AbstractEmailBuilderService {
    @Autowired
    UserEmailVerificationTokenService userEmailVerificationTokenService;
    @Autowired
    UserService userService;

    @Override
    public void build(AbstractInternalEmail email) throws Exception {
        if (!(email instanceof UserEmailVerificationEmail userEmailVerificationEmail)) {
            throw new IllegalArgumentException("Invalid email type");
        }

        User user = userService.getUserById(userEmailVerificationEmail.getUserId());

        UserEmailVerificationToken userEmailVerificationToken = userEmailVerificationTokenService.createUserEmailVerificationToken(
                user,
                user.getEmail()
        );

        Context context = new Context();
        context.setVariable("userEmailVerificationToken", userEmailVerificationToken.getValue());

        render(userEmailVerificationEmail, context);
    }
}
