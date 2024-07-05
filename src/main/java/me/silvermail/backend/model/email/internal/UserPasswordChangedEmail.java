package me.silvermail.backend.model.email.internal;

public class UserPasswordChangedEmail extends AbstractUserEmail {
    @Override
    public String getSubjectKey() {
        return "template.email.user-password-changed.subject";
    }

    @Override
    public String getTemplateName() {
        return "user-password-changed";
    }
}