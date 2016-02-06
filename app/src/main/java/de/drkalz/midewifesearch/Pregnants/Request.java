package de.drkalz.midewifesearch.Pregnants;

/**
 * Created by Alex on 31.01.16.
 */
public class Request {

    private String dateOfBirth;
    private String midwifeID;
    private String requesterID;

    public Request() {
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getMidwifeID() {
        return midwifeID;
    }

    public void setMidwifeID(String midwifeID) {
        this.midwifeID = midwifeID;
    }

    public String getRequesterID() {
        return requesterID;
    }

    public void setRequesterID(String requesterID) {
        this.requesterID = requesterID;
    }
}
