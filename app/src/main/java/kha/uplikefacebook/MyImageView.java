package kha.uplikefacebook;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Kha on 4/6/2016.
 */
public class MyImageView extends ImageView {
    private String mPostId;

    public String getPostId() {
        return mPostId;
    }

    public void setPostId(String postId) {
        this.mPostId = postId;
    }

    public MyImageView(Context context) {
        super(context);
    }

    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


}
