package de.drkalz.midwifesearch.Pregnants;

import java.util.Date;

/**
 * Created by Alex on 31.01.16.
 */
public class Request {

    private Date dateOfBirth;
    private String midwifeID;

    public Request() {
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getMidwifeID() {
        return midwifeID;
    }

    public void setMidwifeID(String midwifeID) {
        this.midwifeID = midwifeID;
    }

}
