package ga.imagination.lib;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ga.imagination.R;

public class TagView extends RelativeLayout {

    /**
     * tag list
     */
    private List<Tag> mTags = new ArrayList<>();
    private List<Tag> mCheckTags = new ArrayList<>();

    private LayoutInflater mInflater;

    /**
     * listener
     */
    private OnTagClickListener mClickListener;
    private OnTagDeleteListener mDeleteListener;

    /**
     * view size param
     */
    private int mWidth;

    /**
     * layout initialize flag
     */
    private boolean mInitialized = false;

    /**
     * custom layout param
     */
    int lineMargin;
    int tagMargin;
    int textPaddingLeft;
    int textPaddingRight;
    int textPaddingTop;
    int texPaddingBottom;


    private boolean singleLine = false;


    /**
     * constructor
     */
    public TagView(Context ctx) {
        super(ctx, null);
        initialize(ctx, null, 0);
    }

    /**
     * constructor
     */
    public TagView(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        initialize(ctx, attrs, 0);
    }

    /**
     * constructor
     */
    public TagView(Context ctx, AttributeSet attrs, int defStyle) {
        super(ctx, attrs, defStyle);
        initialize(ctx, attrs, defStyle);
    }

    /**
     * initalize instance
     */
    private void initialize(Context ctx, AttributeSet attrs, int defStyle) {
        mInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewTreeObserver mViewTreeObserber = getViewTreeObserver();
        mViewTreeObserber.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (!mInitialized) {
                    mInitialized = true;
                    drawTags();
                }
            }
        });

        // get AttributeSet
        TypedArray typeArray = ctx.obtainStyledAttributes(attrs, R.styleable.TagView, defStyle, defStyle);
        this.lineMargin = (int) typeArray.getDimension(R.styleable.TagView_lineMargin, Utils.dpToPx(this.getContext(), Constants.DEFAULT_LINE_MARGIN));
        this.tagMargin = (int) typeArray.getDimension(R.styleable.TagView_tagMargin, Utils.dpToPx(this.getContext(), Constants.DEFAULT_TAG_MARGIN));
        this.textPaddingLeft = (int) typeArray.getDimension(R.styleable.TagView_textPaddingLeft, Utils.dpToPx(this.getContext(), Constants.DEFAULT_TAG_TEXT_PADDING_LEFT));
        this.textPaddingRight = (int) typeArray.getDimension(R.styleable.TagView_textPaddingRight, Utils.dpToPx(this.getContext(), Constants.DEFAULT_TAG_TEXT_PADDING_RIGHT));
        this.textPaddingTop = (int) typeArray.getDimension(R.styleable.TagView_textPaddingTop, Utils.dpToPx(this.getContext(), Constants.DEFAULT_TAG_TEXT_PADDING_TOP));
        this.texPaddingBottom = (int) typeArray.getDimension(R.styleable.TagView_textPaddingBottom, Utils.dpToPx(this.getContext(), Constants.DEFAULT_TAG_TEXT_PADDING_BOTTOM));
        this.singleLine = typeArray.getBoolean(R.styleable.TagView_showSingleLine, singleLine);
        typeArray.recycle();
    }


    /**
     * onSizeChanged
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        if (width <= 0) return;
        mWidth = getMeasuredWidth();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawTags();
    }

    /**
     * tag draw
     */
    private void drawTags() {

        if (!mInitialized) {
            return;
        }

        // clear all tag
        removeAllViews();

        // layout padding left & layout padding right
        float total = getPaddingLeft() + getPaddingRight();

        int listIndex = 1;// List Index
        int index_bottom = 1;// The RestParam to add below
        int index_header = 1;// The header tag of this line
        Tag tag_pre = null;
        for (final Tag item : mTags) {
            final int position = listIndex - 1;
            // inflate tag layout
            CheckableLinearLayout tagLayout = (CheckableLinearLayout) mInflater.inflate(R.layout.tagview_item, null);
            tagLayout.setClickable(item.checkable);
            tagLayout.setCheckable(item.checkable);
            tagLayout.setChecked(item.checkable && item.isChecked);
            tagLayout.setId(listIndex);
            tagLayout.setBackgroundDrawable(getSelector(item));
            // tag text
            TextView tagView = (TextView) tagLayout.findViewById(R.id.tv_tag_item_contain);
            if (item.isChecked) {
                tagView.setTextColor(item.tagTextPressColor);
            } else {
                tagView.setTextColor(getColorState(item));
            }
            tagView.setText(item.text);
            //tagView.setPadding(textPaddingLeft, textPaddingTop, textPaddingRight, texPaddingBottom);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tagView.getLayoutParams();
            params.setMargins(textPaddingLeft, textPaddingTop, textPaddingRight, texPaddingBottom);
            tagView.setLayoutParams(params);
            tagView.setSingleLine(true);
            tagView.setTextSize(TypedValue.COMPLEX_UNIT_SP, item.tagTextSize);
            tagLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClickListener != null) {
                        if (mClickListener.onTagClick(item, position))
                            if (item.isChecked)
                                mCheckTags.add(item);
                            else mCheckTags.remove(item);
                        drawTags();
                    }
                }
            });

            // calculate　of tag layout width
            float tagWidth = tagView.getPaint().measureText(item.text) + textPaddingLeft + textPaddingRight + tagView.getPaddingLeft() + tagView.getPaddingRight();
            // tagView padding (left & right)
            if (mWidth < tagWidth + Utils.dpToPx(this.getContext(), Constants.LAYOUT_WIDTH_OFFSET) && !singleLine) {
                tagView.setSingleLine(false);
                tagView.setWidth(mWidth - Utils.dpToPx(this.getContext(), Constants.LAYOUT_WIDTH_OFFSET));
            }
            // deletable text
            TextView deletableView = (TextView) tagLayout.findViewById(R.id.tv_tag_item_delete);
            if (item.isDeletable) {
                deletableView.setVisibility(View.VISIBLE);
                deletableView.setText(item.deleteIcon);
                int offset = Utils.dpToPx(getContext(), 2f);
                deletableView.setPadding(offset, textPaddingTop, textPaddingRight + offset, texPaddingBottom);
                /*params = (LinearLayout.LayoutParams) deletableView.getLayoutParams();
                params.setMargins(offset, textPaddingTop, textPaddingRight+offset, texPaddingBottom);
				deletableView.setLayoutParams(params);*/
                deletableView.setTextColor(item.deleteIndicatorColor);
                deletableView.setTextSize(TypedValue.COMPLEX_UNIT_SP, item.deleteIndicatorSize);
                deletableView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TagView.this.remove(position);
                        if (mDeleteListener != null) {
                            mDeleteListener.onTagDeleted(item, position);
                        }
                    }
                });
                tagWidth += deletableView.getPaint().measureText(item.deleteIcon) + textPaddingLeft + textPaddingRight;
                // deletableView Padding (left & right)
            } else {
                deletableView.setVisibility(View.GONE);
            }

            LayoutParams tagParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            //tagParams.setMargins(0, 0, 0, 0);

            //add margin of each line
            tagParams.bottomMargin = lineMargin;

            if (mWidth <= total + tagWidth + Utils.dpToPx(this.getContext(), Constants.LAYOUT_WIDTH_OFFSET)) {
                if (singleLine)
                    break;
                //need to add in new line
                tagParams.addRule(RelativeLayout.BELOW, index_bottom);
                // initialize total param (layout padding left & layout padding right)
                total = getPaddingLeft() + getPaddingRight();
                index_bottom = listIndex;
                index_header = listIndex;
            } else {
                //no need to new line
                tagParams.addRule(RelativeLayout.ALIGN_TOP, index_header);
                //not header of the line
                if (listIndex != index_header) {
                    tagParams.addRule(RelativeLayout.RIGHT_OF, listIndex - 1);
                    tagParams.leftMargin = tagMargin;
                    total += tagMargin;
                    if (tag_pre.tagTextSize < item.tagTextSize) {
                        index_bottom = listIndex;
                    }
                }


            }
            total += tagWidth;
            addView(tagLayout, tagParams);
            tag_pre = item;
            listIndex++;

        }

    }

    private Drawable getSelector(@NonNull Tag tag) {
        if (tag.background != null) return tag.background;
        StateListDrawable states = new StateListDrawable();
        GradientDrawable gd_normal = new GradientDrawable();
        gd_normal.setColor(tag.layoutColor);
        gd_normal.setCornerRadius(tag.radius);
        if (tag.layoutBorderSize > 0) {
            gd_normal.setStroke(Utils.dpToPx(getContext(), tag.layoutBorderSize), tag.layoutBorderColor);
        }
        GradientDrawable gd_press = new GradientDrawable();
        gd_press.setColor(tag.layoutColorPress);
        gd_press.setCornerRadius(tag.radius);
        states.addState(new int[]{android.R.attr.state_pressed}, gd_press);
        states.addState(new int[]{android.R.attr.state_checked}, gd_press);
        //must add state_pressed first，or state_pressed will not take effect
        states.addState(new int[]{}, gd_normal);
        return states;
    }

    public ColorStateList getColorState(@NonNull Tag tag) {
        int states[][] = {{android.R.attr.state_checked}, {android.R.attr.state_checked}, {}};
        int[] color = {tag.tagTextPressColor, tag.tagTextPressColor, tag.tagTextColor};
        return new ColorStateList(states, color);
    }


    //----------------- public methods  -----------------//

    /**
     */
    public void addTag(Tag tag) {
        mTags.add(tag);
        drawTags();
    }

    public void addTags(String[] tags) {
        if (tags == null) return;
        mTags.clear();
        for (int i = 0; i < tags.length; i++) {
            Tag tag = new Tag(i, tags[i]);
            tag.checkable = false;
            addTag(tag);
        }
        processCheckedTags();
    }

    public void addTags(Tag[] tags) {
        if (tags == null || tags.length == 0)
            return;
        mTags.clear();
        mTags.addAll(Arrays.asList(tags));
        drawTags();
    }

    /**
     * get tag list
     *
     * @return mTags TagObject List
     */
    public List<Tag> getTags() {
        return mTags;
    }

    public List<Tag> getCheckTags() {
        return this.mCheckTags;
    }

    private void processCheckedTags() {
        mCheckTags.clear();
        for (Tag tag : mTags) {
            if (tag.isChecked)
                mCheckTags.add(tag);
        }
    }

    private void removeCheckedTag(Tag tag) {
        mCheckTags.remove(tag);
    }

    /**
     * remove tag
     */
    public void remove(int position) {
        mTags.remove(position);
        drawTags();
    }

    public void removeAllTags() {
        mTags.clear();
        drawTags();
    }


    public int getLineMargin() {
        return lineMargin;
    }

    public void setLineMargin(float lineMargin) {
        this.lineMargin = Utils.dpToPx(getContext(), lineMargin);
    }

    public int getTagMargin() {
        return tagMargin;
    }

    public void setTagMargin(float tagMargin) {
        this.tagMargin = Utils.dpToPx(getContext(), tagMargin);
    }

    public int getTextPaddingLeft() {
        return textPaddingLeft;
    }

    public void setTextPaddingLeft(float textPaddingLeft) {
        this.textPaddingLeft = Utils.dpToPx(getContext(), textPaddingLeft);
    }

    public int getTextPaddingRight() {
        return textPaddingRight;
    }

    public void setTextPaddingRight(float textPaddingRight) {
        this.textPaddingRight = Utils.dpToPx(getContext(), textPaddingRight);
    }

    public int getTextPaddingTop() {
        return textPaddingTop;
    }

    public void setTextPaddingTop(float textPaddingTop) {
        this.textPaddingTop = Utils.dpToPx(getContext(), textPaddingTop);
    }

    public int getTexPaddingBottom() {
        return texPaddingBottom;
    }

    public void setTexPaddingBottom(float texPaddingBottom) {
        this.texPaddingBottom = Utils.dpToPx(getContext(), texPaddingBottom);
    }

    /**
     * setter for OnTagSelectListener
     */
    public void setOnTagClickListener(OnTagClickListener clickListener) {
        mClickListener = clickListener;
    }

    /**
     * setter for OnTagDeleteListener
     */
    public void setOnTagDeleteListener(OnTagDeleteListener deleteListener) {
        mDeleteListener = deleteListener;
    }

    public TagView setSingleLine(boolean singleLine) {
        this.singleLine = singleLine;
        return this;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Log.d(getClass().getSimpleName(), "onSaveInstanceState");
        return super.onSaveInstanceState();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        Log.d(getClass().getSimpleName(), "onRestoreInstanceState");
    }
}
