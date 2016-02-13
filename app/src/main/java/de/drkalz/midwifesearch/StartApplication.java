package de.drkalz.midwifesearch;

import android.app.Application;

import com.firebase.client.AuthData;

/**
 * Created by Alex on 03.02.16.
 */
public class StartApplication extends Application {

    private static StartApplication singleInstance = null;
    private boolean isMidwife;
    private AuthData authData;
    private String userEmail;
    private String fullUserName;

    public static StartApplication getInstance() {
        return singleInstance;
    }

    public boolean isMidwife() {
        return isMidwife;
    }

    public void setMidwife(boolean midwife) {
        isMidwife = midwife;
    }

    public AuthData getAuthData() {
        return authData;
    }

    public void setAuthData(AuthData authData) {
        this.authData = authData;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getFullUserName() {
        return fullUserName;
    }

    public void setFullUserName(String fullUserName) {
        this.fullUserName = fullUserName;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleInstance = this;
    }
}
