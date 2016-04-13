package kha.uplikefacebook;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Created by Kha on 4/1/2016.
 */
public class LoginToUpLikeFragment extends Fragment {
    private static final String MESSAGE_PUBLISH_POST_SUCCESS = "You published post through application and that is liked";
    private TextView mTxtUser;
    private EditText mEditLinkPost;
    private Button mBtnLoginLogout, mBtnUpLike, mBtnShowAllPosts, mBtnPublishPosts;
    private CallbackManager mCallbackManager;
    private String mIdUser = null;
    private RecyclerView mRecyclerViewPost;
    private List<Post> mPostList = null;
    private CustomRecyclerAdapter mAdapterViewPost;
    private ProfilePictureView mProfilePictureView;
    private ProfileTracker mProfileTracker;
    private MyDatabaseHelper mMyDatabaseHelper;
    private TextToSpeech mTTSMessage;
    private String mRegBtn = " ";

    public static LoginToUpLikeFragment newInstance() {
        LoginToUpLikeFragment fragment = new LoginToUpLikeFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity());
        Log.d("+++kiemtra", "on create");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMyDatabaseHelper = new MyDatabaseHelper(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_to_uplike, container, false);
        mTTSMessage=new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    mTTSMessage.setLanguage(Locale.UK);
                    Log.d("texttospeech", "onInit: ");
                }
            }
        });
        mRecyclerViewPost = (RecyclerView) view.findViewById(R.id.posts_recycler);
        mRecyclerViewPost.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        mTxtUser = (TextView) view.findViewById(R.id.txt_user_id);
        mBtnLoginLogout = (Button) view.findViewById(R.id.btn_logout);
        mBtnUpLike = (Button) view.findViewById(R.id.btn_uplike);
        mBtnShowAllPosts = (Button) view.findViewById(R.id.btn_show_posts);
        mBtnPublishPosts = (Button) view.findViewById(R.id.btn_publish_post);
        mEditLinkPost = (EditText) view.findViewById(R.id.edit_link_posts);
        mEditLinkPost.setTextIsSelectable(false);
        mBtnLoginLogout.setFocusable(true);
        mBtnLoginLogout.requestFocus();
        addEvents();
        mProfilePictureView = (ProfilePictureView) view.findViewById(R.id.image);
        trackingProfile();
        setLoginManager();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mProfilePictureView.requestFocus();
    }

    private void addEvents() {
        mBtnLoginLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        mBtnUpLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPublishPermissions();
            }
        });
        mEditLinkPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message("Input link posts need up like");
            }
        });
        mBtnShowAllPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRegBtn = "showAllPost";
                requestPublishPermissions();
            }
        });
        mBtnPublishPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createInputMessage();
            }
        });
    }

    private void logout() {
        if (mBtnLoginLogout.getText().equals("logout")) {
            mIdUser = null;
            mTxtUser.setText("");
            mEditLinkPost.setText("");
            mEditLinkPost.setVisibility(View.INVISIBLE);
            mBtnUpLike.setVisibility(View.INVISIBLE);
            mBtnPublishPosts.setVisibility(View.INVISIBLE);
            LoginManager.getInstance().logOut();
            mProfilePictureView.setProfileId("default");
            mProfilePictureView.setVisibility(View.INVISIBLE);
            mBtnShowAllPosts.setVisibility(View.INVISIBLE);
            if (mPostList != null) mAdapterViewPost.removeAll();
            mBtnLoginLogout.setText("login");
        } else {
            requestLoginManager();
        }
    }


    private void trackingProfile() {
        Profile pr = Profile.getCurrentProfile();
        if (pr != null) {
            mIdUser = pr.getId();
            mBtnLoginLogout.setVisibility(View.VISIBLE);
            mBtnLoginLogout.setText("logout");
            mEditLinkPost.setVisibility(View.VISIBLE);
            mEditLinkPost.setTextIsSelectable(false);
            mBtnUpLike.setVisibility(View.VISIBLE);
            mBtnShowAllPosts.setVisibility(View.VISIBLE);
            mProfilePictureView.setVisibility(View.VISIBLE);
            mProfilePictureView.setProfileId(pr.getId());
            mTxtUser.setText(pr.getName());
        }
        mProfileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(
                    Profile oldProfile,
                    Profile currentProfile) {
                if (currentProfile != null) mTxtUser.setText(currentProfile.getName());
            }
        };
        mProfileTracker.startTracking();
    }

    private void setLoginManager() {
        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("oke", "success");
                if (mIdUser == null) {
                    message("login success");
                    mIdUser = loginResult.getAccessToken().getUserId();
                    mBtnLoginLogout.setVisibility(View.VISIBLE);
                    mBtnLoginLogout.setText("logout");
                    mBtnUpLike.setVisibility(View.VISIBLE);
                    mEditLinkPost.setVisibility(View.VISIBLE);
                    mBtnShowAllPosts.setVisibility(View.VISIBLE);
                    mProfilePictureView.setVisibility(View.VISIBLE);
                    mProfilePictureView.setProfileId(mIdUser);
                } else {
                    UserFacebook userFacebook = new UserFacebook();
                    userFacebook.setmUserId(loginResult.getAccessToken().getUserId());
                    userFacebook.setmAppID(loginResult.getAccessToken().getApplicationId());
                    userFacebook.setmAccessToken(loginResult.getAccessToken().getToken());
                    if (mMyDatabaseHelper.isExistUserFacebook(userFacebook.getmUserId())) {
                        mMyDatabaseHelper.updateUser(userFacebook);
                    } else mMyDatabaseHelper.addUser(userFacebook);
                    if (mRegBtn.equals("showAllPost")) loadALLPosts();
                    else {
                        upLikeFace();
                    }
                }
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                message("login failed");
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        Log.d("+++kiemtra", "onActivityResult");
    }

    private void requestLoginManager() {
        LoginManager.getInstance().logInWithReadPermissions(
                this,
                Arrays.asList("public_profile", "user_likes", "user_status", "user_friends", " user_posts",
                        "user_photos", "user_videos"));
    }


    private void requestPublishPermissions() {
        // Requesting publish permissions
        LoginManager.getInstance().logInWithPublishPermissions(this, Arrays.asList("publish_actions"));
    }

    private void loadALLPosts() {
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/feed",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        Log.d("loadallpostrespone", "onCompleted: " + response.getRawResponse());
                        try {
                            JSONArray jsonArData = (JSONArray) response.getJSONObject().get("data");
                            mPostList = new ArrayList<Post>();
                            for (int i = 0; i < jsonArData.length(); i++) {
                                JSONObject jsonOb = (JSONObject) jsonArData.get(i);
                                String stringJson = jsonOb.toString();
                                Post post = new Post();
                                post.setmPostId(jsonOb.get("id") + "");
                                if (stringJson.contains("message")) {
                                    post.setmPostContext(jsonOb.get("message") + "");
                                } else post.setmPostContext(jsonOb.get("story") + "");
                                mPostList.add(post);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (NullPointerException n) {
                            n.printStackTrace();
                            mPostList = new ArrayList<Post>();
                            warning();

                        }
                        mAdapterViewPost = new CustomRecyclerAdapter(mPostList, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                upLikeFace(((MyImageView) v).getPostId());
                            }
                        },
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        createInputComment(((MyImageView) v).getPostId());
                                    }
                                });
                        mRecyclerViewPost.setAdapter(mAdapterViewPost);
                        message("show success");
                        mBtnPublishPosts.setVisibility(View.VISIBLE);
                    }
                }
        ).executeAsync();
    }

    private void upLikeFace(String postID) {
        List<UserFacebook> userList = mMyDatabaseHelper.getAllUserFacebook();
        for (UserFacebook user : userList) {
            AccessToken accessTokenLike = new AccessToken(user.getmAccessToken(),
                    user.getmAppID(),
                    user.getmUserId(),
                    null,
                    null,
                    null,
                    null,
                    null);
            new GraphRequest(accessTokenLike, "/" + postID + "/likes",
                    null,
                    HttpMethod.POST,
                    new GraphRequest.Callback() {

                        @Override
                        public void onCompleted(GraphResponse response) {

                            if (response.getRawResponse() != null) {
                                Log.d("graprequest", "response" + response.getRawResponse());
                                String resSuccess = "{\"success\":true}";
                                if (response.getRawResponse().equals(resSuccess)) {
                                    message("Like success");
                                }
                            }
                        }
                    }).executeAsync();
        }
    }

    private void upLikeFace() {
        if(toPostId(mEditLinkPost.getText().toString()) == null)
            message("Can't find posts with your link input");
        List<UserFacebook> userList = mMyDatabaseHelper.getAllUserFacebook();
        for (UserFacebook user : userList) {
            AccessToken accessTokenLike = new AccessToken(user.getmAccessToken(),
                    user.getmAppID(),
                    user.getmUserId(),
                    null,
                    null,
                    null,
                    null,
                    null);
            new GraphRequest(accessTokenLike, "/" + accessTokenLike.getApplicationId()
                    + "_" + toPostId(mEditLinkPost.getText().toString()) + "/likes",
                    null,
                    HttpMethod.POST,
                    new GraphRequest.Callback() {

                        @Override
                        public void onCompleted(GraphResponse response) {

                            if (response.getRawResponse() != null) {
                                Log.d("graprequest", "response" + response.getRawResponse());
                                String resSuccess = "{\"success\":true}";
                                if (response.getRawResponse().equals(resSuccess)) {
                                    message("like success");
                                }
                            }

                        }
                    }).executeAsync();
        }
    }

    private String toPostId(String linkPost) {
        if (linkPost.contains("facebook.com")) return null;
        String postId;
        if (linkPost.contains("fbid")) {
            postId = linkPost.split("=")[1];
            postId = postId.split("&")[0];
        } else postId = null;

        return postId;
    }

    private void createInputComment(final String postId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Input comment");
        final EditText editInput = new EditText(getActivity());
        editInput.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(editInput);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                upCommentFace(postId, editInput.getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    private void upCommentFace(String postID, String comment) {
        List<UserFacebook> userList = mMyDatabaseHelper.getAllUserFacebook();
        for (UserFacebook user : userList) {
            AccessToken accessTokenLike = new AccessToken(user.getmAccessToken(),
                    user.getmAppID(),
                    user.getmUserId(),
                    null,
                    null,
                    null,
                    null,
                    null);
            Bundle params = new Bundle();
            params.putString("message", comment);
            new GraphRequest(accessTokenLike, "/" + postID + "/comments",
                    params,
                    HttpMethod.POST,
                    new GraphRequest.Callback() {

                        @Override
                        public void onCompleted(GraphResponse response) {

                            if (response.getRawResponse() != null) {
                                if (response.getRawResponse().contains("id")) {
                                    message("commnet success");
                                }
                            }

                        }

                    }).executeAsync();
        }

    }


    private void createInputMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Input message");
        final EditText editInput = new EditText(getActivity());
        editInput.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(editInput);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                publishPosts(editInput.getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    private void publishPosts(String message) {
        Bundle params = new Bundle();
        params.putString("message", message);
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/feed",
                params,
                HttpMethod.POST,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        try {
                            Log.d("myPublishPosts", "onCompleted: " + (response.getJSONObject().get("id")).toString());
                            upLikeFace((response.getJSONObject().get("id")).toString());
                            mTTSMessage.speak(MESSAGE_PUBLISH_POST_SUCCESS, TextToSpeech.QUEUE_FLUSH, null);
                            loadALLPosts();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
    }

    public void warning() {
        Toast.makeText(getActivity(), "Your network is disconnect", Toast.LENGTH_LONG).show();
    }

    public void message(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

}
