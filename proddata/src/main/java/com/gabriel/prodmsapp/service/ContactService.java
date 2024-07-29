package com.gabriel.prodmsapp.service;

import com.gabriel.prodmsapp.model.Contact;

public interface ContactService {
    Contact[] getContacts() throws Exception;

    Contact getContact(Integer id) throws Exception;

    Contact create(Contact contact) throws Exception;

    Contact update(Contact contact) throws Exception;

    void delete(Integer id) throws Exception;
}
