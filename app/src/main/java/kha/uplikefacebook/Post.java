package kha.uplikefacebook;

import java.io.Serializable;

/**
 * Created by Kha on 4/6/2016.
 */
public class Post implements Serializable {
    private String mPostId;
    private String mPostContext;

    public String getmPostContext() {
        return mPostContext;
    }

    public void setmPostContext(String mPostContext) {
        this.mPostContext = mPostContext;
    }

    public String getmPostId() {
        return mPostId;
    }

    public void setmPostId(String mPostId) {
        this.mPostId = mPostId;
    }
}
