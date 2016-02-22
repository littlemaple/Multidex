package com.medzone.mcloud.sync;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.medzone.framework.task.BaseTask;

public abstract class BaseSyncController<T> {

	public static final String				TAG					= "espresso";
	private BlockingQueue<BaseTask>	mQueue				= new LinkedBlockingQueue<>();
	private CloudTaskExecutor				mExecutor			= null;
	private Handler							handler				= new Handler(Looper.getMainLooper());
	private Runnable						pollRunnable		= null;
	private long							intervalTimemillis	= 5 * 60 * 1000;

	private boolean							isPolling			= false;

	public BaseSyncController() {
	}

	public void start() {
		stop();
		initAction();
		prepareExecutor();
	}

	/**
	 * 设置后台轮询时间间隔
	 * 
	 * @param intervalTimemillis
	 */
	public void setPollingInterval(long intervalTimemillis) {
		this.intervalTimemillis = intervalTimemillis;
	}

	private void initAction() {
		pollRunnable = new Runnable() {

			@Override
			public void run() {
				postTask();
				Log.d(getClass().getSimpleName(), "start next action");
				if (isPolling) handler.postDelayed(this, intervalTimemillis);
			}
		};
		handler.removeCallbacks(pollRunnable);
		handler.post(pollRunnable);
	}

	public void stopPolling() {
		Log.i(TAG, "stop polling");
		this.isPolling = false;
		handler.removeCallbacks(pollRunnable);
	}

	public void setAutoPolling() {
		this.isPolling = true;
		handler.post(pollRunnable);
	}

	public void stop() {
		Log.d(TAG, "stop component");
		if (mExecutor == null) return;
		handler.removeCallbacks(pollRunnable);
		mExecutor.quit();
	}

	private void prepareExecutor() {
		Log.e(TAG, "prepareExecutor");
		mExecutor = new CloudTaskExecutor(mQueue);
		mExecutor.start();
	}

	public void postTask(BaseTask task) {
		if (mQueue == null) {
			Log.e(TAG, "please the request queue initial");
			return;
		}
		if (task == null) throw new NullPointerException("the task can not be null");
		mQueue.add(task);
	}

	public void postTask() {
		this.postTask(createTask());
	}

	public void refresh() {
		this.postTask();
	}

	protected abstract BaseTask createTask();

}
