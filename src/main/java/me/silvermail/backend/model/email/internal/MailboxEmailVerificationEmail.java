package me.silvermail.backend.model.email.internal;

public class MailboxEmailVerificationEmail extends AbstractInternalEmail {

    protected String mailboxId;

    @Override
    public String getSubjectKey() {
        return "template.email.mailbox-email-verification.subject";
    }

    @Override
    public String getTemplateName() {
        return "mailbox-email-verification";
    }

    public String getMailboxId() {
        return mailboxId;
    }

    public void setMailboxId(String mailboxId) {
        this.mailboxId = mailboxId;
    }
}