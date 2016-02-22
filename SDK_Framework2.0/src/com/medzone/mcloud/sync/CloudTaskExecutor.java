package com.medzone.mcloud.sync;

import java.util.concurrent.BlockingQueue;

import com.medzone.framework.task.BaseTask;

public class CloudTaskExecutor extends BlockingExecutor<BaseTask> {

	public CloudTaskExecutor(BlockingQueue<BaseTask> mQueue) {
		super(mQueue);
	}

	@Override
	protected void execute(BaseTask t) {
		t.execute();
	}

}
