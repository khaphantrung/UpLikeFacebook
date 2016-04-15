package kha.uplikefacebook;

import java.io.Serializable;

/**
 * Created by Kha on 4/6/2016.
 */
public class Post implements Serializable {
    private String postId;
    private String postContext;

    public String getPostContext() {
        return postContext;
    }

    public void setPostContext(String postContext) {
        this.postContext = postContext;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}
