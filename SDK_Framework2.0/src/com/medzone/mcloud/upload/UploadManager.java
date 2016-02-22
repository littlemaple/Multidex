package com.medzone.mcloud.upload;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;

import com.medzone.mcloud.event.EventUploadDef;
import com.medzone.framework.data.bean.Account;
import com.medzone.framework.util.Args;
import com.medzone.mcloud.data.bean.dbtable.UploadEntity;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import de.greenrobot.event.EventBus;

/**
 * @author Robert
 */
public class UploadManager {

    private static UploadManager sInstance;
    public static final String TAG = "UploadManager";

    public static final int UPLOAD_FAILED = -1;
    public static final int UPLOAD_STARTED = 1;
    public static final int UPLOAD_COMPLETE = 2;

    // 设置空闲形成保持存活的时间
    private static final int KEEP_ALIVE_TIME = 1;
    // 设置时间单位
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
    // 被初始化的线程数量
    private static final int CORE_POOL_SIZE = 8;
    // 允许容纳的线程数量
    private static final int MAXIMUM_POOL_SIZE = 8;

    private final BlockingQueue<Runnable> mUploadWorkQueue;

    private final Queue<UploadTask> mUploadTaskWorkQueue;

    private final ThreadPoolExecutor mUploadThreadPool;

    private Handler mHandler;
    private Account account;

    static {
        sInstance = new UploadManager();
    }

    public void init(@NonNull Account account) {
        this.account = account;
    }

    public Account getAttachAccount() {
        return this.account;
    }

    private UploadManager() {
        final UploadCache cache = new UploadCache();
        mUploadWorkQueue = new LinkedBlockingDeque<>();
        mUploadTaskWorkQueue = new LinkedBlockingDeque<>();
        mUploadThreadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT, mUploadWorkQueue);
        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                UploadTask task = (UploadTask) msg.obj;
                switch (msg.what) {
                    case UPLOAD_STARTED:
                        if (!isValid(task)) return;
                        Log.i(TAG, "--->将该条记录保存在数据库中,id:" + task.getEntity().getId() + ",fileName:" + task.getEntity().getFileName() + ",state:" + task.getEntity().getState());
                        if (task.getEntity().getId() == null) {
                            task.getEntity().invalidate();
                            cache.flush(task.getEntity());
                        } else {
                            Log.e(TAG, "--->已经保存在数据库，不再进行再次保存--->检查是否对象引用发生改变");
                        }
                        break;
                    case UPLOAD_COMPLETE:
                        if (!isValid(task)) return;
                        EventBus.getDefault().post(new EventUpload(task.getEntity().getFileName(), task.getEntity().getLocalFilePath(), task.getEntity().getRemotePath(), UPLOAD_COMPLETE));
                        task.getEntity().setState(UploadEntity.STATE_ACCEPTED);
                        Log.i(TAG, "--->将该条标记成完成状态,id:" + task.getEntity().getId() + ",fileName:" + task.getEntity().getFileName() + ",state:" + task.getEntity().getState());
                        if (task.getEntity().getId() != null) {
                            task.getEntity().invalidate();
                            cache.flush(task.getEntity());
                        } else {
                            Log.e(TAG, "--->不应该存在id为空的记录");
                        }
                        recycleTask(task);
                        break;
                    case UPLOAD_FAILED:
                        if (!isValid(task)) return;
                        EventBus.getDefault().post(new EventUpload(task.getEntity().getFileName(), task.getEntity().getLocalFilePath(), task.getEntity().getRemotePath(), UPLOAD_FAILED));
                        Log.i(TAG, "--->该条记录还是running状态,id:" + task.getEntity().getId() + ",fileName:" + task.getEntity().getFileName() + ",state:" + task.getEntity().getState());
                        if (task.getEntity().getId() != null) {
                            task.getEntity().invalidate();
                            cache.flush(task.getEntity());
                        } else {
                            Log.e(TAG, "--->不应该存在id为空的记录");
                        }
                        recycleTask(task);
                        break;
                    default:
                        super.handleMessage(msg);
                }
                EventBus.getDefault().post(new EventUploadDef(msg.what));
            }
        };
    }

    private boolean isValid(UploadTask task) {
        if (task == null) {
            Log.e(TAG, "-->upload task is null");
            return false;
        }
        if (task.getEntity() == null) {
            Log.e(TAG, "--->task.getEntity()==null");
            return false;
        }
        return true;
    }

    public static UploadManager getInstance() {
        return sInstance;
    }

    public void handleState(UploadTask task, int state) {

        switch (state) {
            case UPLOAD_STARTED:
                task.getEntity().setState(UploadEntity.STATE_RUNNING);
                break;
            case UPLOAD_FAILED:
                // 通常是未获取到下块实体，或者是某块连续失败3次。
                task.getEntity().setState(UploadEntity.STATE_PENDING);
                break;
            case UPLOAD_COMPLETE:
                // 表示整个大文件已经上传完成
                task.getEntity().setState(UploadEntity.STATE_ACCEPTED);
                break;
            default:
                break;
        }
        mHandler.obtainMessage(state, task).sendToTarget();
    }

    public static void cancelAll() {
        /*
         * Creates an array of tasks that's the same size as the task work queue
		 */
        UploadTask[] taskArray = new UploadTask[sInstance.mUploadWorkQueue.size()];
        // Populates the array with the task objects in the queue
        sInstance.mUploadWorkQueue.toArray(taskArray);
        // Stores the array length in order to iterate over the array
        int taskArraylen = taskArray.length;
        synchronized (sInstance) {

            // Iterates over the array of tasks
            for (int taskArrayIndex = 0; taskArrayIndex < taskArraylen; taskArrayIndex++) {

                // Gets the task's current thread
                Thread thread = taskArray[taskArrayIndex].getCurrentThread();

                // if the Thread exists, post an interrupt to it
                if (null != thread) {
                    thread.interrupt();
                }
            }
        }
    }

    public static UploadTask startUpload(UploadEntity entity) {

        Args.notNull(entity, "entity");

        UploadTask uploadTask = sInstance.mUploadTaskWorkQueue.poll();

        if (null == uploadTask) {
            uploadTask = new UploadTask();
        }

        uploadTask.initUploadTask(entity);

        switch (entity.getState()) {
            case UploadEntity.STATE_CANCELED:
                Args.check(false, "Note：取消事件在版本中尚未启用");
                break;
            case UploadEntity.STATE_ACCEPTED:
                sInstance.handleState(uploadTask, UPLOAD_COMPLETE);
                break;
            case UploadEntity.STATE_PENDING:
            case UploadEntity.STATE_RUNNING:
                sInstance.mUploadThreadPool.execute(uploadTask.getUploadRunnable());
                break;
            default:
                Args.check(false, "错误的状态值传入：" + entity.getState() + "");
                break;
        }
        return uploadTask;
    }

    public static void removeUpload(UploadEntity entity) {

        Args.notNull(entity, "entity");
        Iterator<UploadTask> iterator = sInstance.mUploadTaskWorkQueue.iterator();
        while (iterator.hasNext()) {
            UploadTask uploadTask = iterator.next();
            Args.notNull(uploadTask, "uploadTask");
            UploadEntity uploadEntity = uploadTask.getEntity();
            Args.notNull(uploadEntity, "uploadEntity");
            if (uploadEntity.equals(entity)) {
                removeUpload(uploadTask);
                break;
            }
        }
    }

    static void removeUpload(UploadTask uploadTask) {

        if (null != uploadTask) {

            synchronized (sInstance) {
                // Gets the Thread that the downloader task is running on
                Thread thread = uploadTask.getCurrentThread();
                // If the Thread exists, posts an interrupt to it
                if (null != thread) {
                    thread.interrupt();
                }
            }
            /*
             * Removes the download Runnable from the ThreadPool. This opens a
			 * Thread in the ThreadPool's work queue, allowing a task in the
			 * queue to start.
			 */
            sInstance.mUploadThreadPool.remove(uploadTask.getUploadRunnable());
        }

    }

    void recycleTask(UploadTask uploadTask) {

        if (null != uploadTask) {

            uploadTask.recycle();

            mUploadTaskWorkQueue.offer(uploadTask);
        }

    }


}
