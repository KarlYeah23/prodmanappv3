package com.gabriel.prodmsv.ServiceImpl;

import com.gabriel.prodmsv.model.Contact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ContactService {
    Logger logger = LoggerFactory.getLogger(ContactService.class);
    static ContactService service = null;
    @Value("${service.api.endpoint}")
    private String endpointUrl = "http://localhost:8080/api/contact";
    RestTemplate restTemplate = null;

    public static ContactService getService() {
        if (service == null) {
            service = new ContactService();
        }
        return service;
    }

    public RestTemplate getRestTemplate() {
        if (restTemplate == null) {
            restTemplate = new RestTemplate();
            List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
            MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
            converter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON));
            messageConverters.add(converter);
            restTemplate.setMessageConverters(messageConverters);
        }
        return restTemplate;
    }

    public Contact getContact(Integer id) {
        String url = endpointUrl + "/" + Integer.toString(id);
        logger.info("getContact: " + url);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<?> request = new HttpEntity<>(null, headers);
        try {
            final ResponseEntity<Contact> response = getRestTemplate().exchange(url, HttpMethod.GET, request, Contact.class);
            return response.getBody();
        } catch (Exception e) {
            logger.error("Error while fetching contact with ID " + id + ": ", e);
            return null;
        }
    }


    public Contact[] getContacts() {
        String url = endpointUrl;
        logger.info("getContacts: " + url);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<?> request = new HttpEntity<>(null, headers);
        final ResponseEntity<Contact[]> response =
                getRestTemplate().exchange(url, HttpMethod.GET, request, Contact[].class);
        return response.getBody();
    }

    public Contact create(Contact contact) {
        String url = endpointUrl;
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Contact> request = new HttpEntity<>(contact, headers);
        final ResponseEntity<Contact> response =
                getRestTemplate().exchange(url, HttpMethod.PUT, request, Contact.class);
        return response.getBody();
    }

    public Contact update(Contact contact) {
        logger.info("update: " + contact.toString());
        String url = endpointUrl;
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Contact> request = new HttpEntity<>(contact, headers);
        final ResponseEntity<Contact> response =
                getRestTemplate().exchange(url, HttpMethod.POST, request, Contact.class);
        return response.getBody();
    }

    public void delete(Integer id) {
        logger.info("delete: " + Integer.toString(id));
        String url = endpointUrl + "/" + Integer.toString(id);
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<?> request = new HttpEntity<>(null, headers);
        getRestTemplate().exchange(url, HttpMethod.DELETE, request, Void.class);
    }
}
