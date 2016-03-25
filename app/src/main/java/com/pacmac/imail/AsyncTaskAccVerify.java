package com.pacmac.imail;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;

/**
 * Created by tqm837 on 3/2/2016.
 */
public class AsyncTaskAccVerify extends AsyncTask<String,Integer, Boolean>{

    public AccVerifyListener mListener = null;
    protected Store store = null;


    public AsyncTaskAccVerify() {

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String[] params) {

        return authenticateAndGetSession(params);
    }

    @Override
    protected void onPostExecute(Boolean result) {
        //super.onPostExecute(result);
            mListener.emailAccVerify(result);
    }



    private boolean authenticateAndGetSession(final String[] userInput){

        Properties properties = new Properties();
        properties.put("mail.store.protocol", userInput[0]);
        properties.put("mail.imaps.host", userInput[1]);
        properties.put("mail.imaps.port", userInput[2]);
        properties.put("mail.imaps.starttls.enable", "true");

        Session emailSession = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userInput[3], userInput[4]);
            }
        });

        try {
            store = emailSession.getStore(userInput[0]);
            store.connect();
            //Log.d("TAG", "STORE IS CONNECTED: " + store.isConnected());



            // load folders in SQLITE
            List<FolderRecord> listFolder = FolderRecord.find(FolderRecord.class, "user_name = ?", userInput[3]);
            Folder[] folders = store.getDefaultFolder().list("*");
            for (Folder folder : folders) {
                boolean isFolderInDB = false;

                if ((folder.getType() & Folder.HOLDS_MESSAGES) != 0) {
                    for (FolderRecord folderStored : listFolder) {

                        if (folderStored.getFriendlyName().equals(folder.getName())) {
                            isFolderInDB = true;
                            break;
                        }
                    }
                    if (!isFolderInDB) {
                        FolderRecord folderRecord = new FolderRecord(folder.getFullName(), userInput[3], folder.getName());
                        folderRecord.save();
                        Log.d("TAG", folderRecord.getFriendlyName() + " SAVE");
                    }
                }
            }

            if(store.isConnected()) {
                //close the store and folder objects
                store.close();
                return true;
            }else
                return false;

        } catch (NoSuchProviderException e) {

            e.printStackTrace();
            return false;
        } catch (MessagingException e) {
            e.printStackTrace();   //TODO Display connection error to user
            return false;
        }
    }


    public interface AccVerifyListener {
        void emailAccVerify(boolean isVerified);

    }
}
