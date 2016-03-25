package com.pacmac.imail;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class EmailType extends Fragment {

    private EmailTypeListener mListener;
    private Button pop3Btn, imapBtn, exchangeBtn;
    private final String TYPE_IMAP = "imapType";
    private final String TYPE_POP3 = "pop3Type";
    private final String TYPE_EXCHANGE = "exchangeType";

    public EmailType() {
        // Required empty public constructor
    }

    public static EmailType newInstance() {
        EmailType fragment = new EmailType();
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

        View fragView = inflater.inflate(R.layout.fragment_email_type, container, false);


        pop3Btn = (Button) fragView.findViewById(R.id.pop3Btn);
        imapBtn = (Button) fragView.findViewById(R.id.imapBtn);
        exchangeBtn = (Button) fragView.findViewById(R.id.exchangeBtn);

        pop3Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mListener != null) {
                    mListener.onEmailTypeChosen(POP3Setup.newInstance(), TYPE_POP3);
                }
            }
        });

        imapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mListener != null) {
                    mListener.onEmailTypeChosen(ImapSetup.newInstance(), TYPE_IMAP);
                }
            }
        });

        exchangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onEmailTypeChosen(ExchangeSetup.newInstance(), TYPE_EXCHANGE);
                }
            }
        });

        return fragView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof EmailTypeListener) {
            mListener = (EmailTypeListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement EmailTypeListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface EmailTypeListener {
        // TODO: Update argument type and name
        void onEmailTypeChosen(Fragment fragment,String type);
    }
}
