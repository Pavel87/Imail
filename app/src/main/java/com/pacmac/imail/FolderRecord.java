package com.pacmac.imail;

import com.orm.SugarRecord;

/**
 * Created by pavel on 22/03/16.
 */
public class FolderRecord extends SugarRecord {

    private String folderName = null;
    private String friendlyName = null;
    private String userName = null;

    public FolderRecord() {
    }

    public FolderRecord(String folderName, String userName,String friendlyName) {
        this.folderName = folderName;
        this.userName = userName;
        this.friendlyName = friendlyName;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public String getFolderName() {
        return folderName;
    }

    public String getUserName() {
        return userName;
    }

}
