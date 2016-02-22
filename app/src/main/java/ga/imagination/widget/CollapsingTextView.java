package ga.imagination.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import ga.imagination.R;

/**
 * Created by 44260 on 2016/1/24.
 */
public class CollapsingTextView extends TextView {
    private boolean isCollapsing = true;
    private static final int DEFAULT_LINES = 2;
    private int foldLines = DEFAULT_LINES;

    public CollapsingTextView(Context context) {
        this(context, null);
    }

    public CollapsingTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CollapsingTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attributeSet) {
        setClickable(true);
        if (attributeSet != null) {
            TypedArray array = getContext().obtainStyledAttributes(attributeSet, R.styleable.CollapsingTextView);
            foldLines = array.getInteger(R.styleable.CollapsingTextView_foldLines, DEFAULT_LINES);
            array.recycle();
        }
        processEvent();
    }

    @Override
    public boolean performClick() {
        isCollapsing = !isCollapsing;
        processEvent();
        return super.performClick();
    }

    private void processEvent() {
        setMaxLines(isCollapsing ? foldLines : Integer.MAX_VALUE);
        setEllipsize(isCollapsing ? TextUtils.TruncateAt.END : null);
    }
}