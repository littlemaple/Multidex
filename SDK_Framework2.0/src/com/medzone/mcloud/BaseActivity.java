package com.medzone.mcloud;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.medzone.mcloud.annotation.InjectClick;
import com.medzone.mcloud.annotation.InjectLayout;
import com.medzone.mcloud.annotation.InjectView;
import com.medzone.framework.util.TaskUtil;
import com.umeng.analytics.MobclickAgent;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class BaseActivity extends AppCompatActivity {

    public boolean isActive;

    public BaseActivity() {
        super();
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkClickEvent();
        isActive = true;
    }

    @Override
    public void setSupportActionBar(Toolbar toolbar) {
        String releaseCode = Build.VERSION.RELEASE;
        if (releaseCode != null && releaseCode.contains("4.2.2"))
            return;
        try {
            super.setSupportActionBar(toolbar);
        } catch (Exception e) {
            //WTF os 4.2.2 samsung htc etc
            e.printStackTrace();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /**
     * A SubClass should override the following functions instead of override
     * this function.
     *
     * @see #preInitUI();
     * @see #initUI();
     * @see #postInitUI();
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkInjectLayout();
        preLoadData(savedInstanceState);
        preInitUI();
        initUI();
        postInitUI();
    }

    public void keyBoardCancel() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * This method will be called before {@link #onCreate(Bundle)}
     */
    protected void preLoadData(Bundle savedInstanceState) {

    }

    /**
     * this function will be call in {@link #onCreate(Bundle)} before
     * {@link #initUI()}
     */
    protected void preInitUI() {

    }

    /**
     * this function will be call in {@link #onCreate(Bundle)} to initialize the
     * UI.This is where most initialization should go: calling
     * {@link #setContentView(int)} to inflate the activity's UI, using
     * {@link #findViewById} to programmatically interact with widgets in the UI
     */
    protected void initUI() {

    }

    protected void postInitUI() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        isActive = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        isActive = true;
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isActive = false;
        System.gc();
    }

    public void finishWithAnimation() {
        super.finish();
        // TODO Define exit animation
    }

    @Override
    public void finish() {
        keyBoardCancel();
        super.finish();
    }

    @Override
    public void startActivityFromFragment(Fragment fragment, Intent intent, int requestCode) {
        if (!TaskUtil.isMoveTaskToBack(this, intent))
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        super.startActivityFromFragment(fragment, intent, requestCode);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        if (!TaskUtil.isMoveTaskToBack(this, intent))
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        super.startActivityForResult(intent, requestCode);
    }

    @Override
    public void startActivity(Intent intent) {
        if (!TaskUtil.isMoveTaskToBack(this, intent))
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        super.startActivity(intent);
    }

    protected void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }

    @Override
    public boolean onTouchEvent(android.view.MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void setContentView(int layoutResId) {
        super.setContentView(layoutResId);
        checkInjectView();
    }

    private void checkInjectView() {
        Field[] fields = getClass().getDeclaredFields();
        for (Field field : fields) {
            InjectView annotation = field.getAnnotation(InjectView.class);
            if (annotation != null) {
                int id = annotation.value();
                View view = findViewById(id);
                field.setAccessible(true);
                try {
                    field.set(this, view);
                } catch (IllegalAccessException | IllegalArgumentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }
    }

    private void checkInjectLayout() {
        InjectLayout annotation = getClass().getAnnotation(InjectLayout.class);
        if (annotation != null) {
            int id = annotation.value();
            try {
                Method method = getClass().getMethod("setContentView", int.class);
                method.invoke(this, id);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void checkClickEvent() {
        Method[] methods = getClass().getDeclaredMethods();
        for (Method method : methods) {
            InjectClick annotation = method.getAnnotation(InjectClick.class);
            if (annotation != null) {
                int id = annotation.value();
                if (findViewById(id) == null) continue;
                findViewById(id).setOnClickListener(new ProxyClick(method));
            }
        }
    }

    private class ProxyClick implements View.OnClickListener {

        public Method method;

        private ProxyClick(Method method) {
            this.method = method;
        }

        @Override
        public void onClick(View v) {
            try {
                method.setAccessible(true);
                method.invoke(BaseActivity.this);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

}
