package com.medzone.mcloud.util;

import android.net.Uri;

import com.medzone.mcloud.util.BaseMeasureDataUtil;

/**
 * Created by 44260 on 2015/12/18.
 */
public class FileNameUtil {
    public static String getFileName(int syncId, String type, String subType, String suffix) {
        return type + "-" + subType + "-" + syncId + "-" + BaseMeasureDataUtil.createUID() + "." + suffix;
    }

    public static String getPath(String url) {
        try {
            Uri uri = Uri.parse(url);
            return uri.getPath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }
}
