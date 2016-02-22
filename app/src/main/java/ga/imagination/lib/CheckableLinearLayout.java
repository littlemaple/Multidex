package ga.imagination.lib;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.LinearLayout;

/**
 * Created by 44260 on 2015/12/10.
 */
public class CheckableLinearLayout extends LinearLayout implements Checkable {
    boolean mChecked = false;
    private boolean checkable = true;

    private static final int[] CHECKED_STATE_SET = {android.R.attr.state_checked};

    public CheckableLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckableLinearLayout(Context context) {
        super(context);
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void setChecked(boolean checked) {
        if (mChecked != checked) {
            mChecked = checked;
            refreshDrawableState();
        }
    }

    @Override
    public void toggle() {
        if (!isCheckable())
            return;
        mChecked = !mChecked;
        refreshDrawableState();
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    @Override
    public boolean performClick() {
        toggle();
        return super.performClick();
    }

    public CheckableLinearLayout setCheckable(boolean checkable) {
        this.checkable = checkable;
        return this;
    }

    public boolean isCheckable() {
        return checkable;
    }
}
