package com.pacmac.imail;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by pavel on 23/03/16.
 */

public class FolderArrayAdapter extends BaseAdapter {

    private Context context = null;
    private ArrayList<String> folderNames = null;

    public FolderArrayAdapter(Context context, ArrayList<String> folderNames) {
        this.folderNames = folderNames;
        this.context = context;
    }

    @Override
    public int getCount() {
        return folderNames.size();
    }

    @Override
    public String getItem(int position) {
        return folderNames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /*private view holder class*/
    private class ViewHolder {
        TextView txtTitle;

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.nav_item_view_cust, null);
            holder = new ViewHolder();
            holder.txtTitle = (TextView) convertView.findViewById(R.id.nav_cst_item_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtTitle.setText(getItem(position));

        return convertView;
    }
}
