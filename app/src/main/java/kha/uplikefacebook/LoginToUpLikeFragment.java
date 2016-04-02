package kha.uplikefacebook;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Kha on 4/1/2016.
 */
public class LoginToUpLikeFragment extends Fragment {
    public static LoginToUpLikeFragment newInstance() {
        LoginToUpLikeFragment fragment = new LoginToUpLikeFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_to_uplike,container,false);

        return view;

    }
}
