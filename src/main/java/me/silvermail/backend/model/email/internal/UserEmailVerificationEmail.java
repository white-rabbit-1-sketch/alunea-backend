package me.silvermail.backend.model.email.internal;

public class UserEmailVerificationEmail extends AbstractUserEmail {
    @Override
    public String getSubjectKey() {
        return "template.email.user-email-verification.subject";
    }

    @Override
    public String getTemplateName() {
        return "user-email-verification";
    }
}