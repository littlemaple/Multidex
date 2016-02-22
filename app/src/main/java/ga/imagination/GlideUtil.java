package ga.imagination;

import android.app.Activity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by 44260 on 2016/1/26.
 */
public class GlideUtil {


    public static void display(Activity activity, String uri, int errorResource, ImageView imageView) {
        if (NetUtil.isConnect(activity)) {
            Glide.with(activity).load(uri).error(errorResource).into(imageView);
        } else {
            Glide.with(activity).load(uri).error(errorResource).transform(new GreyTransform(activity)).into(imageView);
        }
    }
}
