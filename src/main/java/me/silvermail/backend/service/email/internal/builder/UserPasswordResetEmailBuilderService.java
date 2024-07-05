package me.silvermail.backend.service.email.internal.builder;

import me.silvermail.backend.model.email.internal.AbstractInternalEmail;
import me.silvermail.backend.model.email.internal.UserPasswordResetEmail;
import me.silvermail.backend.entity.User;
import me.silvermail.backend.entity.token.UserPasswordResetToken;
import me.silvermail.backend.service.UserService;
import me.silvermail.backend.service.token.UserPasswordResetTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

@Service
public class UserPasswordResetEmailBuilderService extends AbstractEmailBuilderService {
    @Autowired
    UserPasswordResetTokenService userPasswordResetTokenService;
    @Autowired
    UserService userService;

    @Override
    public void build(AbstractInternalEmail email) throws Exception {
        if (!(email instanceof UserPasswordResetEmail passwordRestoreEmail)) {
            throw new IllegalArgumentException("Invalid email type");
        }

        User user = userService.getUserById(passwordRestoreEmail.getUserId());

        UserPasswordResetToken userPasswordResetToken = userPasswordResetTokenService.createUserPasswordResetToken(user);

        Context context = new Context();
        context.setVariable("userPasswordResetToken", userPasswordResetToken.getValue());

        render(passwordRestoreEmail, context);
    }
}
