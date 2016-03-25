package com.pacmac.imail;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by tqm837 on 3/3/2016.

 * Temporary class for storing EMAIL account

*/


public class AccountManager {

    Context mContext = null;

    private String userName = null;
    private String password = null;
    private String port = null;
    private String server = null;
    private String type = null;

    private final String IMAIL_PREF = "imailPref";
    private final String EMAIL1_ADDED = "emailAdded1";
    private final String ACC_USER = "acc1User";
    private final String ACC_PASS = "acc1Pass";
    private final String ACC_SERV = "acc1Serv";
    private final String ACC_PORT = "acc1Port";
    private final String ACC_TYPE = "acc1Type";

    public AccountManager(Context context){
        mContext = context;
    }

    public void setProperties(String userName, String password, String server, String port, String type){
        this.userName = userName;
        this.password = password;
        this.port = port;
        this.server = server;
        this.type = type;

        writeSharedPref();
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getPort() {
        return port;
    }

    public String getServer() {
        return server;
    }

    public String getType() {
        return type;
    }

    public void getProperties(){
        SharedPreferences sharedPref = mContext.getSharedPreferences(IMAIL_PREF, Context.MODE_PRIVATE);
        this.userName = sharedPref.getString(ACC_USER, userName);
        this.password = sharedPref.getString(ACC_PASS, password);
        this.server = sharedPref.getString(ACC_SERV, server);
        this.port = sharedPref.getString(ACC_PORT, port);
        this.type = sharedPref.getString(ACC_TYPE, type);

    }


    private void writeSharedPref(){

        SharedPreferences sharedPref = mContext.getSharedPreferences(IMAIL_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(EMAIL1_ADDED, true);
        editor.putString(ACC_USER, userName);
        editor.putString(ACC_PASS, password);
        editor.putString(ACC_SERV, server);
        editor.putString(ACC_PORT, port);
        editor.putString(ACC_TYPE, type);
        editor.commit();

    }

}
