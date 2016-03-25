package com.pacmac.imail;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.Collections;
import java.util.List;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

/**
 * Created by tqm837 on 3/3/2016.
 */
public class AsyncTaskCheckEmails extends AsyncTask<String, Integer, Boolean> {


    public GetEmailsListener mListener = null;
    private Message[] messages;

    public AsyncTaskCheckEmails() {
    }


    @Override
    protected Boolean doInBackground(String... params) {
        return checkEmails(params);
    }

    @Override
    protected void onPostExecute(Boolean result) {
        mListener.emailsLoaded(result);
    }


    private boolean checkEmails(String[] emailAcc) {
        try {
            String userName = emailAcc[0];
            String password = emailAcc[1];
            String server = emailAcc[2];
            String port = emailAcc[3];
            String type = emailAcc[4];

            //create properties field
            Properties properties = new Properties();

            properties.put("mail.imaps.host", server);   // TODO careful if server will be POP3 or EXCHANGE
            properties.put("mail.imaps.port", port);
            properties.put("mail.imaps.starttls.enable", "true");
            Session emailSession = Session.getDefaultInstance(properties);

            //create the POP3 store object and connect with the pop server
            Store store = emailSession.getStore(type);
            store.connect(server, userName, password);

            //create the folder object and open it
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            // retrieve the messages from the folder in an array and print it
            messages = emailFolder.getMessages();
            Log.d("TAG", "messages.length---" + messages.length);

            EmailRecord emailRecord = null;
            List<EmailRecord> storedEmails = EmailRecord.listAll(EmailRecord.class);

            boolean isEmailInDb = false;
            for (Message message : messages) {

                //Log.d("TAG", "message: " + message.getContent());

                for (EmailRecord email : storedEmails) {
                    if (email.getEmailId() == message.getMessageNumber()) {
                        isEmailInDb = true;
                        break;
                    }
                }
                if (!isEmailInDb) {
                    emailRecord = new EmailRecord(message.getMessageNumber(), message.getSubject(), message.getReceivedDate());
                    emailRecord.save();
                }
                isEmailInDb = false;
            }

            //close the store and folder objects
            emailFolder.close(false);
            store.close();
            return true;
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
            return false;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public interface GetEmailsListener {
        void emailsLoaded(boolean isReady);
    }

}
