package com.pacmac.imail;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class SetupActivity extends AppCompatActivity implements EmailType.EmailTypeListener {


    private Fragment fragment;
    private final String FRAG_ETYPE = "emailType";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        fragment = EmailType.newInstance();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.frag_setup, fragment, FRAG_ETYPE);
        ft.addToBackStack(FRAG_ETYPE);
        ft.commit();
    }

    @Override
    public void onEmailTypeChosen(Fragment fragment, String fragName) {
        this.fragment = fragment;
        getFragmentManager().beginTransaction().replace(R.id.frag_setup, fragment, fragName).commit();
    }
}
