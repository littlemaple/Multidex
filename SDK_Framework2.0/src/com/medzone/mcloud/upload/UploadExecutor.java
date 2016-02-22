package com.medzone.mcloud.upload;

import java.util.concurrent.BlockingQueue;

import com.medzone.mcloud.sync.BlockingExecutor;
import com.medzone.mcloud.data.bean.dbtable.UploadEntity;

public class UploadExecutor extends BlockingExecutor<UploadEntity> {

	public UploadExecutor(BlockingQueue<UploadEntity> mQueue) {
		super(mQueue);
	}

	@Override
	protected void execute(UploadEntity t) {
		UploadManager.startUpload(t);
	}

}
