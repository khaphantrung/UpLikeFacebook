package kha.uplikefacebook;

import java.io.Serializable;

/**
 * Created by Kha on 4/5/2016.
 */
public class UserFacebook implements Serializable {
    private String userId;
    private String accessToken;
    private String appId;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userID) {
        this.userId = userID;
    }

    public String getmAppID() {
        return appId;
    }

    public void setmAppID(String mAppID) {
        this.appId = mAppID;
    }
}
