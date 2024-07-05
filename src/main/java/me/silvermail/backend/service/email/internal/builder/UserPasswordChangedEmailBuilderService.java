package me.silvermail.backend.service.email.internal.builder;

import me.silvermail.backend.model.email.internal.AbstractInternalEmail;
import me.silvermail.backend.model.email.internal.UserPasswordChangedEmail;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

@Service
public class UserPasswordChangedEmailBuilderService extends AbstractEmailBuilderService {

    @Override
    public void build(AbstractInternalEmail email) throws Exception {
        if (!(email instanceof UserPasswordChangedEmail userPasswordChangedEmail)) {
            throw new IllegalArgumentException("Invalid email type");
        }

        Context context = new Context();

        render(userPasswordChangedEmail, context);
    }
}
