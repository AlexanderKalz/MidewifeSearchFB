package de.drkalz.midwifesearch.Pregnants;

/**
 * Created by Alex on 31.01.16.
 */
public class Request {

    private long dateOfBirth;
    private String midwifeID;

    public Request() {
    }

    public long getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(long dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getMidwifeID() {
        return midwifeID;
    }

    public void setMidwifeID(String midwifeID) {
        this.midwifeID = midwifeID;
    }

}
