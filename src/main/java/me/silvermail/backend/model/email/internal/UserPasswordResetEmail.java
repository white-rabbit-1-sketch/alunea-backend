package me.silvermail.backend.model.email.internal;

public class UserPasswordResetEmail extends AbstractUserEmail {
    @Override
    public String getSubjectKey() {
        return "template.email.user-password-reset.subject";
    }

    @Override
    public String getTemplateName() {
        return "user-password-reset";
    }
}