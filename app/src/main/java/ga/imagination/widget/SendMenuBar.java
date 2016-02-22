package ga.imagination.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;

import ga.imagination.R;


/**
 * Created by 44260 on 2015/12/27.
 */
public class SendMenuBar extends LinearLayout {
    private LinearLayout popLayoutContainer;

    public SendMenuBar(Context context) {
        super(context);
        initComponent();
    }

    public SendMenuBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initComponent();
    }

    public SendMenuBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponent();
    }

    private void initComponent() {
        removeAllViews();
        setFocusable(true);
        setFocusableInTouchMode(true);
        setGravity(Gravity.CENTER_VERTICAL);
        setOrientation(VERTICAL);
        LinearLayout sendLayout = new LinearLayout(getContext());
        sendLayout.setOrientation(HORIZONTAL);
        sendLayout.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams sendParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        sendLayout.setLayoutParams(sendParams);

        final LinearLayout pickLayout = new LinearLayout(getContext());
        pickLayout.setOrientation(HORIZONTAL);
        LinearLayout.LayoutParams pickParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        pickLayout.setBackgroundColor(Color.WHITE);
        pickLayout.setGravity(Gravity.CENTER_VERTICAL);
        pickLayout.setLayoutParams(pickParams);
        pickLayout.setVisibility(View.GONE);
        pickLayout.setId(R.id.layout_add_view);
        pickLayout.removeAllViews();
        pickLayout.addView(getFakeImage());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.weight = 1;
        params.leftMargin = 20;
        params.rightMargin = 20;
        final ImageView imageView = new ImageView(getContext());
        imageView.setPadding(5, 5, 5, 5);
        imageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.message_ic_camera));
        final EditText editText = new EditText(getContext());
        editText.setLayoutParams(params);
        editText.setPadding(20, 20, 20, 20);
        editText.setMaxLines(4);
        editText.setMinLines(1);
        editText.clearFocus();
        editText.setBackgroundResource(R.drawable.shape_edit);
        editText.setImeActionLabel("发送", EditorInfo.IME_ACTION_SEND);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND && onMenuMonitorListener != null)
                    onMenuMonitorListener.onClickSend(editText, editText.getText() == null ? "" : editText.getText().toString());
                return false;
            }
        });
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("SendBar", "" + isSoftInputShow(editText));
                if (pickLayout.getVisibility() == View.GONE) {
                    pickLayout.setVisibility(View.VISIBLE);
                } else {
                    pickLayout.setVisibility(View.GONE);
                }
            }
        });
        final TextView textView = new TextView(getContext());
        textView.setText("发送");
        textView.setTextColor(Color.parseColor("#b9b9b9"));
        textView.setPadding(5, 5, 5, 5);
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onMenuMonitorListener == null)
                    return;
                onMenuMonitorListener.onClickSend(editText, editText.getText().toString());
            }
        });
        sendLayout.addView(imageView);
        sendLayout.addView(editText);
        sendLayout.addView(textView);
        addView(sendLayout);

        addView(pickLayout);
    }

    private onMenuMonitorListener onMenuMonitorListener;

    public void setOnMenuMonitorListener(SendMenuBar.onMenuMonitorListener onMenuMonitorListener) {
        this.onMenuMonitorListener = onMenuMonitorListener;
    }

    public interface onMenuMonitorListener {
        void onClickCamera(View view);

        void onClickSend(View view, String text);
    }

    private boolean isSoftInputShow(EditText editText) {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm.isActive(editText);
    }

    private void showSoftInputMethod(EditText editText) {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, 0);
    }

    private void hideSoftInputMethod() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindowToken(), 0);
    }

    //TODO 增加外部处理事件
    public void registerParentView(View view) {

    }

    public void addImage(String path) {
        final LinearLayout viewGroup = (LinearLayout) findViewById(R.id.layout_add_view);
        if (viewGroup == null || TextUtils.isEmpty(path))
            return;
        final ImageView imageView = new ImageView(getContext());
        Glide.with(getContext()).load(path).into(imageView);
        imageView.setPadding(40, 40, 40, 40);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

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
                        viewGroup.removeView(imageView);
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.show();
                return false;
            }
        });
        viewGroup.addView(imageView, 0);
    }

    public void addImage(Drawable drawable) {
        final LinearLayout viewGroup = (LinearLayout) findViewById(R.id.layout_add_view);
        if (viewGroup == null || drawable == null)
            return;
        final ImageView imageView = new ImageView(getContext());
        imageView.setImageDrawable(drawable);
        imageView.setPadding(40, 40, 40, 40);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        viewGroup.removeView(imageView);
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.show();
                return false;
            }
        });
        viewGroup.addView(imageView, 0);
    }

    private ImageView getFakeImage() {
        ImageView fakeImage = new ImageView(getContext());
        fakeImage.setPadding(80, 80, 80, 80);
        fakeImage.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.message_ic_camera));
        fakeImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onMenuMonitorListener != null)
                    onMenuMonitorListener.onClickCamera(v);
            }
        });
        return fakeImage;
    }

    public int getImageCount() {
        final LinearLayout viewGroup = (LinearLayout) findViewById(R.id.layout_add_view);
        if (viewGroup == null)
            return 0;
        return viewGroup.getChildCount() - 1;
    }

}
