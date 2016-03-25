package com.pacmac.imail;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class ImapSetup extends Fragment implements AsyncTaskAccVerify.AccVerifyListener {


    private String userName;
    private String password;
    private String port;
    private String server;
    private final String IMAIL_PREF = "imailPref";
    private final String EMAIL1_ADDED = "emailAdded";
    private final String EMAIL_TYPE = "imaps";

    EditText userEdit;
    EditText passEdit;
    EditText serverEdit;
    EditText portEdit;

    private ProgressDialog progressDialog;


    private OnFragmentInteractionListener mListener;


    public ImapSetup() {
        // Required empty public constructor
    }

    public static ImapSetup newInstance() {
        ImapSetup fragment = new ImapSetup();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_imap_setup, container, false);

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        userEdit = (EditText) view.findViewById(R.id.editEmail);
        passEdit = (EditText) view.findViewById(R.id.editPass);
        serverEdit = (EditText) view.findViewById(R.id.editServer);
        portEdit = (EditText) view.findViewById(R.id.editPort);
        Button connect = (Button) view.findViewById(R.id.connectBtn);

        userEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String emailCheck = ((EditText) v).getText().toString();
                    if (emailCheck.length() > 0) {
                        try {
                            InternetAddress internetAddress = new InternetAddress(emailCheck);
                            internetAddress.validate();
                            v.setBackgroundColor(Color.TRANSPARENT); // modernize this!
                        } catch (AddressException e) {
                            v.setBackgroundColor(Color.RED);  // modernize this!
                        }
                    }
                }
            }
        });

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                processForm();

                AsyncTaskAccVerify asyncTaskAccVerify = new AsyncTaskAccVerify();
                asyncTaskAccVerify.mListener = ImapSetup.this;
                asyncTaskAccVerify.execute(EMAIL_TYPE, server, port, userName, password);

                progressDialog = new ProgressDialog(getActivity());
                progressDialog.show();
            }
        });

    }

    private void processForm() {
        userName = userEdit.getText().toString();
        password = passEdit.getText().toString();
        port = portEdit.getText().toString();
        server = serverEdit.getText().toString();
    }


    private void setEmailAcc() {

        SharedPreferences sharedPref = getActivity().getSharedPreferences(IMAIL_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(EMAIL1_ADDED, true);
        editor.commit();

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void emailAccVerify(boolean isVerified) {


        if (isVerified) {
            AccountManager accountManager = new AccountManager(getActivity().getApplication());
            accountManager.setProperties(userName,password,server,port, "imaps");
            progressDialog.hide();
            Toast.makeText(getActivity().getApplicationContext(), "Email is connected", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity().getApplicationContext(), StartActivity.class);
            startActivity(intent);
        } else
            progressDialog.hide();
            Toast.makeText(getActivity().getApplicationContext(), "EMAIL EXISTS: " + isVerified, Toast.LENGTH_SHORT).show();
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
