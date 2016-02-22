package ga.imagination.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 44260 on 2016/1/10.
 */
public class ImageGallery extends LinearLayout implements GestureDetector.OnGestureListener {

    private static final int DEF_COUNT = 6;
    private GestureDetector gestureDetector = new GestureDetector(getContext(), this);
    private Map<ImageView, String> cacheName = new HashMap<>();
    private Scroller scroller = new Scroller(getContext());

    public ImageGallery(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ImageGallery(Context context) {
        this(context, null);
    }

    public ImageGallery(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private void init() {
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
    }

    public void setImageUrl(String imageUrl) {
        String[] res = imageUrl.split(",");
        removeAllViews();
        for (final String path : res) {
            ImageView imageView = new ImageView(getContext());
            imageView.setBackgroundColor(Color.WHITE);
            imageView.setPadding(8, 8, 8, 8);
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadImageShower(getContext(), path);
                }
            });
            Picasso.with(getContext()).load(path).into(imageView);
            addView(imageView, 0);
        }
    }

    public void addImage(final String path, String fileName) {
        final ImageView imageView = new ImageView(getContext());
        imageView.setBackgroundColor(Color.WHITE);
        cacheName.put(imageView, fileName);
        imageView.setPadding(8, 8, 8, 8);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImageShower(getContext(), path);
            }
        });
        imageView.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("是否删除图片?");
                builder.setTitle("提示");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeView(imageView);
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.show();
                return false;
            }
        });
        Picasso.with(getContext()).load(path).into(imageView);
        addView(imageView, 0);
    }

    public void addTextView(String text, Drawable top) {
        addTextView(text, null, null, false);
    }

    public void addTextView(String text, Drawable top, OnClickListener listener) {
        addTextView(text, null, top, null, null, listener, false);
    }

    public void addTextView(String text, Drawable left, Drawable top, OnClickListener listener) {
        addTextView(text, left, top, null, listener, false);
    }

    public void addTextView(String text, Drawable left, Drawable top, Drawable right, OnClickListener listener) {
        addTextView(text, left, top, right, null, listener, false);
    }

    public void addTextView(String text, Drawable top, boolean last) {
        addTextView(text, null, null, last);
    }

    public void addTextView(String text, Drawable top, OnClickListener listener, boolean last) {
        addTextView(text, null, top, null, null, listener, last);
    }

    public void addTextView(String text, Drawable left, Drawable top, OnClickListener listener, boolean last) {
        addTextView(text, left, top, null, listener, last);
    }

    public void addTextView(String text, Drawable left, Drawable top, Drawable right, OnClickListener listener, boolean last) {
        addTextView(text, left, top, right, null, listener, last);
    }

    public void addTextView(String text, Drawable left, Drawable top, Drawable right, Drawable bottom, OnClickListener listener, boolean last) {
        TextView textView = new TextView(getContext());
        textView.setText(text);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(8, 8, 8, 8);
        if (left != null)
            left.setBounds(0, 0, left.getMinimumWidth(), left.getMinimumHeight());
        if (right != null)
            right.setBounds(0, 0, right.getMinimumWidth(), right.getMinimumHeight());
        if (top != null)
            top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());
        if (bottom != null)
            bottom.setBounds(0, 0, bottom.getMinimumWidth(), bottom.getMinimumHeight());
        textView.setCompoundDrawables(left, top, right, bottom);
        textView.setCompoundDrawablePadding(5);
        if (listener != null)
            textView.setOnClickListener(listener);
        addView(textView, last ? getChildCount() : 0);
    }

    public void remove(int position) {
        if (position < 0 || position >= getChildCount())
            return;
        View view = getChildAt(position);
        if (view instanceof ImageView)
            cacheName.remove(view);
        removeView(view);
    }

    public void removeLast() {
        if (getChildCount() > 0)
            remove(getChildCount() - 1);
    }

    public void removeFirst() {
        if (getChildCount() > 0)
            remove(0);
    }

    public void removeAll() {
        removeAllViews();
        cacheName.clear();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int count = (getChildCount() == 0 ? 1 : getChildCount()) <= DEF_COUNT ? DEF_COUNT : (getChildCount() == 0 ? 1 : getChildCount());
        int subMeasureSpec = MeasureSpec.makeMeasureSpec(width / count, MeasureSpec.EXACTLY);
        measureChildren(subMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        //TODO 增加水平滚动
//        if (getChildCount() <= 6)
//            return false;
//        float distance = 0;
//        Log.d(getClass().getSimpleName(), "" + distanceX + ",scrollx" + getScrollX());
//        if (getScrollX() > 0)
//            distanceX = 0;
//        scrollBy((int) distanceX, 0);
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (!scroller.computeScrollOffset())
            return;
        scrollTo(scroller.getCurrX(), 0);
        postInvalidate();
    }

    private void loadImageShower(Context context, String path) {
        try {
            Intent intent = new Intent();
            intent.setAction(android.content.Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(path), "image/*");
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getImageTags() {
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof ImageView && cacheName.get(view) != null) {
                buffer.append(cacheName.get(view));
                buffer.append(",");
            }
        }
        if (TextUtils.isEmpty(buffer.toString()))
            return "";
        if (buffer.toString().charAt(buffer.toString().length() - 1) == ',')
            return buffer.toString().substring(0, buffer.toString().length() - 1);
        return buffer.toString();
    }

    public void unInit() {
        cacheName.clear();
    }
}
