package de.drkalz.midewifesearch.Midwifes;

public class AngebotsGebiet {
    private String street, city, country, zip;
    private Double radiusKM;

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

    public Double getRadiusKM() {
        return radiusKM;
    }

    public void setRadiusKM(Double radiusKM) {
        this.radiusKM = radiusKM;
    }
}
