package me.silvermail.backend.controller;

import jakarta.validation.Valid;
import me.silvermail.backend.dto.controller.request.contact.CreateContactRequestDto;
import me.silvermail.backend.dto.controller.response.BaseResponseDto;
import me.silvermail.backend.dto.controller.response.contact.ContactListResponseDto;
import me.silvermail.backend.dto.controller.response.contact.ContactResponseDto;
import me.silvermail.backend.dto.controller.response.contact.UserContactEmailAvailabilityResponseDto;
import me.silvermail.backend.entity.Contact;
import me.silvermail.backend.entity.User;
import me.silvermail.backend.service.ContactService;
import me.silvermail.backend.service.InternalEmailService;
import me.silvermail.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ContactController {
    @Autowired
    protected ContactService contactService;
    @Autowired
    protected UserService userService;
    @Autowired
    protected InternalEmailService emailService;

    @GetMapping(path="/user/{userId}/contact/{email}")
    @PreAuthorize("hasRole('USER') && authentication.getPrincipal().getId() == #user.getId()")
    public UserContactEmailAvailabilityResponseDto isUserContactEmailAvailable(
            @PathVariable("userId") User user,
            @PathVariable String email
    ) {
        Contact contact = this.contactService.getContactByUserIdAndEmail(user.getId(), email);

        UserContactEmailAvailabilityResponseDto userContactEmailAvailabilityResponseDto = new UserContactEmailAvailabilityResponseDto();
        userContactEmailAvailabilityResponseDto.setAvailable(contact == null);

        return userContactEmailAvailabilityResponseDto;
    }

    @PostMapping("/user/{userId}/contact")
    @PreAuthorize("hasRole('USER') && authentication.getPrincipal().getId() == #user.getId()")
    public ContactResponseDto createContact(
            @PathVariable("userId") User user,
            @RequestBody @Valid CreateContactRequestDto createContactRequestDto
    ) {
        Contact contact = contactService.createContact(user, createContactRequestDto.getEmail());

        ContactResponseDto contactResponseDto = new ContactResponseDto();
        contactResponseDto.setContact(contact);

        return contactResponseDto;
    }

    @GetMapping(path="/user/{userId}/contact")
    @PreAuthorize("hasRole('USER') && authentication.getPrincipal().getId() == #user.getId()")
    public ContactListResponseDto getContactListByUserId(
            @PathVariable("userId") User user
    ) {
        List<Contact> contactList = this.contactService.getContactListByUserId(user.getId());

        ContactListResponseDto contactListResponseDto = new ContactListResponseDto();
        contactListResponseDto.setContactList(contactList);

        return contactListResponseDto;
    }

    @PostMapping("/contact/{contactId}/enable")
    @PreAuthorize("hasRole('USER') && authentication.getPrincipal().getId() == #contact.getUser().getId()")
    public BaseResponseDto enableContact(
            @PathVariable("contactId") Contact contact
    ) {
        contact.setEnabled(true);
        contactService.saveContact(contact);

        return new BaseResponseDto();
    }

    @PostMapping("/contact/{contactId}/disable")
    @PreAuthorize("hasRole('USER') && authentication.getPrincipal().getId() == #contact.getUser().getId()")
    public BaseResponseDto disableContact(
            @PathVariable("contactId") Contact contact
    ) {
        contact.setEnabled(false);
        contactService.saveContact(contact);

        return new BaseResponseDto();
    }

    @DeleteMapping("/contact/{contactId}")
    @PreAuthorize("hasRole('USER') && authentication.getPrincipal().getId() == #contact.getUser().getId()")
    public BaseResponseDto removeContact(
            @PathVariable("contactId") Contact contact
    ) {
        contactService.removeContact(contact);

        return new BaseResponseDto();
    }
}