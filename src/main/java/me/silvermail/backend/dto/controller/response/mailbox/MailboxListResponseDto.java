package me.silvermail.backend.dto.controller.response.mailbox;

import me.silvermail.backend.dto.controller.response.BaseResponseDto;
import me.silvermail.backend.entity.Mailbox;

import java.util.List;

public class MailboxListResponseDto extends BaseResponseDto {
    protected List<Mailbox> mailboxList;

    public List<Mailbox> getMailboxList() {
        return mailboxList;
    }

    public void setMailboxList(List<Mailbox> mailboxList) {
        this.mailboxList = mailboxList;
    }
}
