package com.medzone.mcloud.upload;

/**
 * Created by asusu on 2015/12/2.
 */
public class EventUpload {

    public static final int UPLOAD_FAILED = UploadManager.UPLOAD_FAILED;
    public static final int UPLOAD_STARTED = UploadManager.UPLOAD_STARTED;
    public static final int UPLOAD_COMPLETE = UploadManager.UPLOAD_COMPLETE;
    public int uploadState;
    public String localPath;
    public String fileName;
    public String remotePath;


    public EventUpload(String fileName, String localPath,String remotePath, int uploadState) {
        this.fileName = fileName;
        this.localPath = localPath;
        this.uploadState = uploadState;
        this.remotePath = remotePath;
    }

}
