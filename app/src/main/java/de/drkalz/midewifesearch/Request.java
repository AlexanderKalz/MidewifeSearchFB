package de.drkalz.midewifesearch;

import com.firebase.geofire.GeoLocation;

/**
 * Created by Alex on 31.01.16.
 */
public class Request {

    private String dateOfBirth;
    private String requesterID;
    private String midwifeID;
    private GeoLocation mGeoLocation;

    public Request() {
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getRequesterID() {
        return requesterID;
    }

    public void setRequesterID(String requesterID) {
        this.requesterID = requesterID;
    }

    public String getMidwifeID() {
        return midwifeID;
    }

    public void setMidwifeID(String midwifeID) {
        this.midwifeID = midwifeID;
    }

    public GeoLocation getGeoLocation() {
        return mGeoLocation;
    }

    public void setGeoLocation(GeoLocation geoLocation) {
        mGeoLocation = geoLocation;
    }
}
