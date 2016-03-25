package com.pacmac.imail;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class Inbox extends Fragment {

    private OnInboxListener mListener;
    private InboxRecyclerViewAdapter adapter = null;
    private List<EmailRecord> emails = null;
    private SwipeRefreshLayout refreshLayout = null;


    public Inbox() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inbox_list, container, false);

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_layout);
        refreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mListener.refreshInboxEmails();
            }
        });
        Context context = view.getContext();
        emails = EmailRecord.listAll(EmailRecord.class);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new InboxRecyclerViewAdapter(emails);
        recyclerView.setAdapter(adapter);

        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnInboxListener) {
            mListener = (OnInboxListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public void updateInboxList() {
        adapter.updateEmailsInAdapter(EmailRecord.listAll(EmailRecord.class));
        adapter.notifyDataSetChanged();
        refreshLayout.setRefreshing(false);
    }

    public interface OnInboxListener {
        void refreshInboxEmails();
    }
}
