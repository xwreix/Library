package com.lab.library.beans;

import java.sql.Timestamp;

public class Reader {
    private String surname;
    private String name;
    private String patronymic;
    private String passportNumber;
    private Timestamp dateOfBirth;
    private String email;

    public Reader(String surname, String name, String patronymic, String passportNumber, String email) {
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.passportNumber = passportNumber;
        this.email = email;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
