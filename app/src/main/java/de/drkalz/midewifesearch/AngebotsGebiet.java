package de.drkalz.midewifesearch;

import com.firebase.geofire.GeoLocation;

public class AngebotsGebiet {
    private String midwifeID;
    private Double radiusKM;
    private GeoLocation mGeoLocation;

    public AngebotsGebiet() {
    }

    public String getMidwifeID() {
        return midwifeID;
    }

    public void setMidwifeID(String midwifeID) {
        this.midwifeID = midwifeID;
    }

    public Double getRadiusKM() {
        return radiusKM;
    }

    public void setRadiusKM(Double radiusKM) {
        this.radiusKM = radiusKM;
    }

    public GeoLocation getGeoLocation() {
        return mGeoLocation;
    }

    public void setGeoLocation(GeoLocation geoLocation) {
        mGeoLocation = geoLocation;
    }
}
