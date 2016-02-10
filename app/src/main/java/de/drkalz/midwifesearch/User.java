package de.drkalz.midwifesearch;

/**
 * Created by Alex on 01.02.16.
 */
public class User {

    private String firstname;
    private String lastname;
    private String street;
    private String city;
    private String country;
    private String homepage;
    private String telefon;
    private String mobil;
    private String zip;
    private Boolean isMidwife;
    private String eMail;

    public User() {
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getHomepage() {
        return homepage;
    }

    public String getTelefon() {
        return telefon;
    }

    public String getMobil() {
        return mobil;
    }

    public String getZip() {
        return zip;
    }

    public boolean getIsMidwife() {
        return isMidwife;
    }

    public String geteMail() {
        return eMail;
    }

}