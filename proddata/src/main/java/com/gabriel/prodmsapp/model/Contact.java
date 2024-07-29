package com.gabriel.prodmsapp.model;

import lombok.Data;

@Data
public class Contact {
    int id;
    String firstName;
    String lastName;
    String email;
    String phoneNumber;

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}
