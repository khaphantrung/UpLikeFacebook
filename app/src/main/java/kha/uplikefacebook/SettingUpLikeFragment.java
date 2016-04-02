package kha.uplikefacebook;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Kha on 4/1/2016.
 */
public class SettingUpLikeFragment extends Fragment {
    private TextView mTxtFollow, mTxtPublicMode;
    public static SettingUpLikeFragment newInstance() {
        SettingUpLikeFragment fragment = new SettingUpLikeFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_setting_uplikes,container,false);
        mTxtFollow = (TextView) view.findViewById(R.id.txt_setting_follow);
        setLinkTxtFollow();

        mTxtPublicMode = (TextView) view.findViewById(R.id.txt_setting_public_mode);
        setLinkTxtPubLicMode();
        return view;
    }
    private void setLinkTxtFollow(){
        mTxtFollow.setText(Html.fromHtml("<a href=\"http://fb.com/settings?tab=followers\">Setting</a>"));
        mTxtFollow.setMovementMethod(LinkMovementMethod.getInstance());
    }
    private void setLinkTxtPubLicMode(){
        mTxtPublicMode.setText(Html.fromHtml("<a href=\"https://www.facebook.com/settings?tab=privacy&section=composer&view\">Setting</a>"));
        mTxtPublicMode.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
