package kha.uplikefacebook;

import java.io.Serializable;

/**
 * Created by Kha on 4/5/2016.
 */
public class UserFacebook implements Serializable {
    private String mUserId;
    private String mAccessToken;
    private String mAppId;

    public String getmAccessToken() {
        return mAccessToken;
    }

    public void setmAccessToken(String mAccessToken) {
        this.mAccessToken = mAccessToken;
    }

    public String getmUserId() {
        return mUserId;
    }

    public void setmUserId(String userID) {
        this.mUserId = userID;
    }

    public String getmAppID() {
        return mAppId;
    }

    public void setmAppID(String mAppID) {
        this.mAppId = mAppID;
    }
}
