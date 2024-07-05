package me.silvermail.backend.dto.controller.response.contact;

import me.silvermail.backend.dto.controller.response.BaseResponseDto;
import me.silvermail.backend.entity.Contact;

import java.util.List;

public class ContactListResponseDto extends BaseResponseDto {
    protected List<Contact> contactList;

    public List<Contact> getContactList() {
        return contactList;
    }

    public void setContactList(List<Contact> contactList) {
        this.contactList = contactList;
    }
}
