package ga.imagination;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.Log;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * Created by 44260 on 2016/1/26.
 */
public class GreyTransform extends BitmapTransformation {
    private Context context;

    public GreyTransform(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return toGreyScale(pool, toTransform);
    }

    @Override
    public String getId() {
        return getClass().getName();
    }

    public Bitmap toGreyScale(BitmapPool pool, Bitmap bmpOriginal) {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();
        Bitmap bmpGreyScale = pool.get(width, height, Bitmap.Config.RGB_565);
        if (bmpGreyScale == null)
            bmpGreyScale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmpGreyScale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        if (!NetUtil.isConnect(context)) {
            paint.setColorFilter(f);
        }
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGreyScale;
    }
}
