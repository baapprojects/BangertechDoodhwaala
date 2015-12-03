package com.bangertech.doodhwaala.gcm;

/**
 * Created by annutech on 12/1/2015.
 */
import com.bangertech.doodhwaala.utils.AppUrlList;

public interface Config {

    // used to share GCM regId with application server - using php app server
    //static final String APP_SERVER_URL = "http://192.168.1.17/gcm/gcm.php?shareRegId=1";
    static final String APP_SERVER_URL = AppUrlList.ACTION_URL;

    // GCM server using java
    // static final String APP_SERVER_URL =
    // "http://192.168.1.17:8080/GCM-App-Server/GCMNotification?shareRegId=1";

    // Google Project Number
    static final String GOOGLE_PROJECT_ID = "1025506914787";
    static final String MESSAGE_KEY = "message";

}
