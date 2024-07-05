package me.silvermail.backend.dto.controller.response.contact;

import me.silvermail.backend.dto.controller.response.BaseResponseDto;
import me.silvermail.backend.entity.Contact;

public class ContactResponseDto extends BaseResponseDto {
    protected Contact contact;

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }
}
