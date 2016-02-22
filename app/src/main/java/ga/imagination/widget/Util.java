package ga.imagination.widget;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

/**
 * Created by 44260 on 2016/1/14.
 */
public class Util {
    public static Uri createUriFromDrawable(Context context, int drawableId) {
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + context.getResources().getResourcePackageName(drawableId) + "/"
                + context.getResources().getResourceTypeName(drawableId) + "/"
                + context.getResources().getResourceEntryName(drawableId));
    }
}
