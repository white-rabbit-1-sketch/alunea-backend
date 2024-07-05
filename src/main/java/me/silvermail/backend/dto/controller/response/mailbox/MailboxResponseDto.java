package me.silvermail.backend.dto.controller.response.mailbox;

import me.silvermail.backend.dto.controller.response.BaseResponseDto;
import me.silvermail.backend.entity.Mailbox;

public class MailboxResponseDto extends BaseResponseDto {
    protected Mailbox mailbox;

    public Mailbox getMailbox() {
        return mailbox;
    }

    public void setMailbox(Mailbox mailbox) {
        this.mailbox = mailbox;
    }
}
