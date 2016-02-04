package de.drkalz.midewifesearch;

import android.app.Application;

import com.firebase.client.AuthData;

/**
 * Created by Alex on 03.02.16.
 */
public class StartApplication extends Application {

    private static StartApplication singleInstance = null;
    private boolean isMidwife;
    private AuthData authData;

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

    @Override
    public void onCreate() {
        super.onCreate();
        singleInstance = this;
    }
}
