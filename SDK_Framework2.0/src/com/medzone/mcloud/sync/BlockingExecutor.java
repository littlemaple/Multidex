package com.medzone.mcloud.sync;

import java.util.concurrent.BlockingQueue;

import android.util.Log;

public abstract class BlockingExecutor<T> extends Thread {

	public static final String	TAG		= BlockingExecutor.class.getSimpleName();

	private boolean				isStop	= false;
	private BlockingQueue<T>	mQueue;

	public BlockingExecutor(BlockingQueue<T> mQueue) {
		if (mQueue == null) throw new NullPointerException("the queue can not be null");
		this.mQueue = mQueue;
	}

	@Override
	public void run() {
		while (!isStop) {
			try {
				T t = mQueue.take();
				execute(t);
				Log.d(TAG, "execute a task");
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	protected abstract void execute(T t);

	public boolean isRunning() {
		if (!isStop && !isInterrupted()) {
			return true;
		}
		return false;
	}

	public void quit() {
		Log.d(TAG, "quite the thread");
		isStop = true;
		interrupt();
	}
}
