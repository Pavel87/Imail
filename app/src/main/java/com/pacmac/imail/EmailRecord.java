package com.pacmac.imail;

import com.orm.SugarRecord;

import java.util.Date;

/**
 * Created by pavel on 19/03/16.
 */

public class EmailRecord extends SugarRecord {

    private int emailId;
    private String subject;
    private Date date;


    public EmailRecord() {

    }

    public EmailRecord(int id, String subject, Date date) {
        this.emailId = id;
        this.subject = subject;
        this.date = date;
    }


    public int getEmailId() {
        return emailId;
    }

    public String getSubject() {
        return subject;
    }

    public Date getDate() {
        return date;
    }

}
