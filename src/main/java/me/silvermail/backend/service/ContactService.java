package me.silvermail.backend.service;

import me.silvermail.backend.entity.Contact;
import me.silvermail.backend.entity.User;
import me.silvermail.backend.entity.alias.AbstractAlias;
import me.silvermail.backend.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ContactService {
    @Autowired
    protected ContactRepository contactRepository;

    public Contact getContactById(String id) {
        return contactRepository.findById(id).orElse(null);
    }

    public Contact getContactByUserIdAndEmail(String userId, String email) {
        return contactRepository.findByUserIdAndEmail(userId, email);
    }

    public List<Contact> getContactListByUserId(String userId) {
        return contactRepository.findAllByUserId(userId);
    }

    public List<Contact> getContactListByUserIdAndEmailList(String userId, List<String> emailList) {
        return contactRepository.findByUserIdAndEmailList(userId, emailList);
    }

    public Map<String, Contact> getContactMapByUserIdAndEmailList(String userId, List<String> emailList) {
        return getContactListByUserIdAndEmailList(userId, emailList)
                .stream()
                .collect(Collectors.toMap(Contact::getEmail, contact -> contact));
    }

    public Contact createContact(User user, String email) {
        email = email.trim();

        Contact existedContact = getContactByUserIdAndEmail(user.getId(), email);
        if (existedContact != null) {
            throw new IllegalArgumentException("Contact already exists");
        }

        Contact contact = new Contact();
        contact.setUser(user);
        contact.setEmail(email);
        contact.setCreateTime(new Date());

        contactRepository.save(contact);

        return contact;
    }

    public void saveContact(Contact contact) {
        contactRepository.save(contact);
    }

    public void removeContact(Contact contact) {
        contactRepository.delete(contact);
    }
}