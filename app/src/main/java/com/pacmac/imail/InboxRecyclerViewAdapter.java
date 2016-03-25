package com.pacmac.imail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;


public class InboxRecyclerViewAdapter extends RecyclerView.Adapter<InboxRecyclerViewAdapter.ViewHolder> {


    private List<EmailRecord> emails = null;

    public InboxRecyclerViewAdapter(List<EmailRecord> emails) {
        this.emails = emails;
        Collections.reverse(this.emails);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_inbox_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.mIdView.setText(position+1 +"");
        holder.mSubjectView.setText(emails.get(position).getSubject());
        holder.mDate.setText(emails.get(position).getDate().toString());

    }

    @Override
    public int getItemCount() {
        return emails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mSubjectView;
        public final TextView mDate;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mSubjectView = (TextView) view.findViewById(R.id.subject);
            mDate = (TextView) view.findViewById(R.id.date);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mSubjectView.getText() + "'";
        }
    }

    public void updateEmailsInAdapter(List<EmailRecord> emails){
        this.emails = emails;
        Collections.reverse(this.emails);
    }

}
