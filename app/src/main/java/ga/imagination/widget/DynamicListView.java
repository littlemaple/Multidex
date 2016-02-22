package ga.imagination.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import ga.imagination.R;
import ga.imagination.databinding.SubscribeListItemDoctorBinding;
import ga.imagination.module.Doctor;

/**
 * Created by 44260 on 2016/1/25.
 */
public class DynamicListView extends LinearLayout {

    public DynamicListView(Context context) {
        this(context, null);
    }

    public DynamicListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DynamicListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        setOrientation(VERTICAL);
        if (attrs != null) {
            TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.DynamicListView);
            array.recycle();
        }
    }

    public void setContent(List<Doctor> list) {
        removeAllViews();
        for (int i = 0; i < (list == null ? 0 : list.size()); i++) {
            if (list.get(i) == null)
                continue;
            View view = LayoutInflater.from(getContext()).inflate(R.layout.subscribe_list_item_doctor, null);
            SubscribeListItemDoctorBinding binding = DataBindingUtil.bind(view);
            binding.setItem(list.get(i));
            addView(view);
            View divideView = new View(getContext());
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2);
            divideView.setLayoutParams(params);
            divideView.setBackgroundColor(Color.parseColor("#dcdcdc"));
            addView(divideView);
        }
    }

    public void setDLayout(Integer resourceId) {
        Log.d(getClass().getSimpleName(), "" + resourceId);
    }
}
