package com.gabriel.prodmsapp.serviceimpl;

import com.gabriel.prodmsapp.entity.ContactData;
import com.gabriel.prodmsapp.model.Contact;
import com.gabriel.prodmsapp.repository.ContactDataRepository;
import com.gabriel.prodmsapp.service.ContactService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ContactServiceImpl implements ContactService {
    Logger logger = LoggerFactory.getLogger(ContactServiceImpl.class);

    @Autowired
    ContactDataRepository contactDataRepository;

    @Override
    public Contact[] getContacts() {
        List<ContactData> contactsData = new ArrayList<>();
        List<Contact> contacts = new ArrayList<>();
        contactDataRepository.findAll().forEach(contactsData::add);

        for (ContactData contactData : contactsData) {
            Contact contact = new Contact();
            contact.setId(contactData.getId());
            contact.setFirstName(contactData.getFirstName());
            contact.setLastName(contactData.getLastName());
            contact.setPhoneNumber(contactData.getPhoneNumber());
            contact.setEmail(contactData.getEmail());
            contacts.add(contact);
        }

        return contacts.toArray(new Contact[0]);
    }

    @Override
    public Contact create(Contact contact) {
        logger.info("add: Input" + contact.toString());
        ContactData contactData = new ContactData();
        contactData.setFirstName(contact.getFirstName());
        contactData.setLastName(contact.getLastName());
        contactData.setPhoneNumber(contact.getPhoneNumber());
        contactData.setEmail(contact.getEmail());

        contactData = contactDataRepository.save(contactData);
        logger.info("add: Input" + contactData.toString());

        Contact newContact = new Contact();
        newContact.setId(contactData.getId());
        newContact.setFirstName(contactData.getFirstName());
        newContact.setLastName(contactData.getLastName());
        newContact.setPhoneNumber(contactData.getPhoneNumber());
        newContact.setEmail(contactData.getEmail());
        return newContact;
    }

    @Override
    public Contact update(Contact contact) {
        ContactData contactData = new ContactData();
        contactData.setId(contact.getId());
        contactData.setFirstName(contact.getFirstName());
        contactData.setLastName(contact.getLastName());
        contactData.setPhoneNumber(contact.getPhoneNumber());
        contactData.setEmail(contact.getEmail());

        contactData = contactDataRepository.save(contactData);

        Contact newContact = new Contact();
        newContact.setId(contactData.getId());
        newContact.setFirstName(contactData.getFirstName());
        newContact.setLastName(contactData.getLastName());
        newContact.setPhoneNumber(contactData.getPhoneNumber());
        newContact.setEmail(contactData.getEmail());
        return newContact;
    }

    @Override
    public Contact getContact(Integer id) {
        logger.info("Input id >> " + Integer.toString(id));
        Optional<ContactData> optional = contactDataRepository.findById(id);
        if (optional.isPresent()) {
            logger.info("Is present >> ");
            Contact contact = new Contact();
            ContactData contactDatum = optional.get();
            contact.setId(contactDatum.getId());
            contact.setFirstName(contactDatum.getFirstName());
            contact.setLastName(contactDatum.getLastName());
            contact.setPhoneNumber(contactDatum.getPhoneNumber());
            contact.setEmail(contactDatum.getEmail());
            return contact;
        }
        logger.info("Failed  >> unable to locate contact");
        return null;
    }

    @Override
    public void delete(Integer id) {
        logger.info("Input >> " + Integer.toString(id));
        Optional<ContactData> optional = contactDataRepository.findById(id);
        if (optional.isPresent()) {
            ContactData contactDatum = optional.get();
            contactDataRepository.delete(contactDatum);
            logger.info("Successfully deleted >> " + contactDatum.toString());
        } else {
            logger.info("Failed  >> unable to locate contact id: " + Integer.toString(id));
        }
    }
}
