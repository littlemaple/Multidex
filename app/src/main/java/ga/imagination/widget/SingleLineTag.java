package ga.imagination.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import ga.imagination.lib.Utils;


public class SingleLineTag extends LinearLayout {

    private String tags;
    private final int horizontal_margin = Utils.dpToPx(getContext(), 8);
    private final int vertical_margin = Utils.dpToPx(getContext(), 5);

    public SingleLineTag(Context context) {
        this(context, null);
    }

    public SingleLineTag(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SingleLineTag(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        drawTags();
    }

    public void setTag(String tags) {
        this.tags = tags;
        drawTags();
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childWidthCount = 0;
        int realCount = 0;
        for (int i = 0; i < getChildCount(); i++) {
            TextView childView = (TextView) getChildAt(i);
            if (childView.getMeasuredWidth() == 0) {
                childView.layout(0, 0, 0, 0);
                continue;
            }
            int left = realCount * horizontal_margin + childWidthCount;
            childWidthCount += getTextViewWidth(childView);
            Log.d("tag", "[ pos: " + i + "  width:" + childView.getMeasuredWidth() + " calWidth:" + getTextViewWidth(childView) + " widthCount:" + childWidthCount + " count: " + getChildCount() + " ,text:" + childView.getText() + " ]");
            int right = childWidthCount + realCount * horizontal_margin;
            if (right >= getMeasuredWidth()) {
                childView.layout(0, 0, 0, 0);
                continue;
            }
            int top = vertical_margin;
            int bottom = childView.getMeasuredHeight() + vertical_margin;
            childView.layout(left, top, right, bottom);
            realCount += 1;

        }
    }

    private float getTextViewWidth(@NonNull View view) {
        if (!(view instanceof TextView))
            return view.getMeasuredWidth();
        TextView textView = (TextView) view;
        float textWidth = 0;
        if (textView.getPaint() != null && !TextUtils.isEmpty(textView.getText())) {
            textWidth = textView.getPaint().measureText(textView.getText().toString());
        }
        return textWidth + textView.getPaddingLeft() + textView.getPaddingRight();
    }

    private void drawTags() {
        removeAllViews();
        if (TextUtils.isEmpty(tags))
            return;
        setOrientation(HORIZONTAL);
        String[] res = tags.split(" ");
        for (String re : res) {
            if (TextUtils.isEmpty(re))
                continue;
            TextView textView = new TextView(getContext());
            textView.setText(re);
            textView.setTextColor(Color.parseColor("#37ced2"));
            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.setMargins(Utils.dpToPx(getContext(), 8), Utils.dpToPx(getContext(), 5), Utils.dpToPx(getContext(), 8), Utils.dpToPx(getContext(), 5));
            textView.setLayoutParams(params);
            textView.setSingleLine();
            textView.setTextSize(14);
            textView.setPadding(Utils.dpToPx(getContext(), 15), Utils.dpToPx(getContext(), 5), Utils.dpToPx(getContext(), 15), Utils.dpToPx(getContext(), 5));
            textView.setBackgroundDrawable(getSelector());
            addView(textView);
        }
    }


    private Drawable getSelector() {
        StateListDrawable states = new StateListDrawable();
        GradientDrawable gd_normal = new GradientDrawable();
        gd_normal.setColor(Color.parseColor("#ffffff"));
        gd_normal.setCornerRadius(85);
        gd_normal.setStroke(Utils.dpToPx(getContext(), 0.9f), Color.parseColor("#37ced2"));
        GradientDrawable gd_press = new GradientDrawable();
        gd_press.setCornerRadius(85);
        gd_press.setStroke(Utils.dpToPx(getContext(), 0.9f), Color.parseColor("#37ced2"));
        states.addState(new int[]{android.R.attr.state_pressed}, gd_press);
        states.addState(new int[]{android.R.attr.state_checked}, gd_press);
        //must add state_pressed first，or state_pressed will not take effect
        states.addState(new int[]{}, gd_normal);
        return states;
    }
}
