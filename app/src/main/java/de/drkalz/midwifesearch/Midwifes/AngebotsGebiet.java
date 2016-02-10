package de.drkalz.midwifesearch.Midwifes;

public class AngebotsGebiet {
    private String street, city, country, zip, midwifeID;
    private Double radiusInM;

    public AngebotsGebiet() {
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getMidwifeID() {
        return midwifeID;
    }

    public void setMidwifeID(String midwifeID) {
        this.midwifeID = midwifeID;
    }

    public Double getRadiusInM() {
        return radiusInM;
    }

    public void setRadiusInM(Double radiusInM) {
        this.radiusInM = radiusInM;
    }
}
