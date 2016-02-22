package com.medzone.mcloud.data.bean.java;

import android.text.TextUtils;

import com.medzone.framework.network.HttpMethod;
import com.medzone.framework.util.Args;
import com.medzone.mcloud.data.bean.dbtable.UploadEntity;

import org.apache.http.entity.InputStreamEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;


/**
 * 
 * 分块上传的实体类，用于定义本块的范围以及上传的地址。<br/>
 * 
 * <p>
 * <code>
 * 	  "url": "http://db.mcloudlife.com/attachments/23/hello.xls/0,301",
		  "method": "PUT",
		  "off": 0,
		  "len": 301,
		  "finished": "N"
	</code>
 * </p>
 * 
 * @author Robert
 * @category 分块上传实体
 */
public  class UploadBlockEntity {

	/**
	 * 分块请求地址，通常将分块上传到该地址。
	 */
	private String			mRequestPath;
	/**
	 * 分块请求方式，参加{@link HttpMethod}
	 */
	private HttpMethod		mMethod;
	private int				mOffset;
	private int				mLength;

	/**
	 * 本分块依附的父实体，单独只有分块没有实体，则分块是没有意义的。
	 */
	private UploadEntity mParentEntity;

	/**
	 * 
	 * 必须保障在第一时间为分块绑定他所属的父实体。
	 * 
	 * @param entity
	 *            本分块依附的父实体
	 */
	public void bindParentEntity(UploadEntity entity) {
		mParentEntity = entity;
	}

	public UploadEntity getParentEntity() {
		return mParentEntity;
	}

	public UploadBlockEntity() {

	}

	void setOffset(int offset) {
		mOffset = offset;
	}

	public int getOffset() {
		return mOffset;
	}

	void setLength(int length) {
		mLength = length;
	}

	public int getLength() {
		return mLength;
	}

	void setRequestPath(String requestPath) {
		mRequestPath = requestPath;
	}

	public String getRequestPath() {
		return mRequestPath;
	}

	void setMethod(String method) {
		mMethod = convertHttpMethod(method);
	}

	public HttpMethod getMethod() {
		return mMethod;
	}

	public long blockFileSize() {
		return mLength - mOffset;
	}

	private HttpMethod convertHttpMethod(String method) {

		HttpMethod httpRet = HttpMethod.POST;
		;
		Args.notNull(method, "method");

		if (method.equalsIgnoreCase("POST")) {
			httpRet = HttpMethod.POST;
		} else if (method.equalsIgnoreCase("PUT")) {
			httpRet = HttpMethod.PUT;
		} else if (method.equalsIgnoreCase("POST")) {
			httpRet = HttpMethod.POST;
		} else if (method.equalsIgnoreCase("DELETE")) {
			httpRet = HttpMethod.DELETE;
		} else if (method.equalsIgnoreCase("GET")) {
			httpRet = HttpMethod.GET;
		} else if (method.equalsIgnoreCase("TRACE")) {
			httpRet = HttpMethod.TRACE;
		} else if (method.equalsIgnoreCase("HEAD")) {
			httpRet = HttpMethod.HEAD;
		} else if (method.equalsIgnoreCase("OPTIONS")) {
			httpRet = HttpMethod.OPTIONS;
		}
		return httpRet;
	}

	public InputStreamEntity getBlockEntity() throws IOException {

		Args.notNull(mParentEntity, "parentEntity");
		Args.notNegative(mOffset, "mOffset");
		Args.notNegative(mLength, "mLength");

		final File file = mParentEntity.getFile();
		Args.notNull(file, "file");

		final int MAX_BUFFER_SIZE = 1024;
		byte[] buffer = new byte[MAX_BUFFER_SIZE];
		int readedLength = 0;
		ByteArrayOutputStream outputStream = null;
		RandomAccessFile mRandomAccessFile = null;
		synchronized (file) {
			try {
				mRandomAccessFile = new RandomAccessFile(file, "r");
				mRandomAccessFile.seek(mOffset);

				outputStream = new ByteArrayOutputStream();

				while (readedLength < mLength) {

					int byteCount;
					if (mLength - readedLength > MAX_BUFFER_SIZE) {
						byteCount = MAX_BUFFER_SIZE;
					} else {
						byteCount = mLength - readedLength;
					}
					int len = mRandomAccessFile.read(buffer, 0, byteCount);
					outputStream.write(buffer);
					readedLength += len;
				}
			} finally {
				if (mRandomAccessFile != null) {
					mRandomAccessFile.close();
				}
				if (outputStream != null) {
					outputStream.close();
				}
			}
		}
		return new InputStreamEntity(new ByteArrayInputStream(outputStream.toByteArray()), mLength);
	}

	public static UploadBlockEntity parseBlockEntity(UploadEntity parentEntity, JSONObject json) {

		Args.notNull(parentEntity, "parentEntity");

		UploadBlockEntity entity = new UploadBlockEntity();
		entity.bindParentEntity(parentEntity);
		try {

			if (json.has("errcode") && !json.isNull("errcode")) {
				throw new IllegalArgumentException(json.getString("errcode") + ":" + json.getString("errmsg"));
			}
			if (json.has("url") && !json.isNull("url")) {
				entity.setRequestPath(json.getString("url"));
			}
			if (json.has("method") && !json.isNull("method")) {
				entity.setMethod(json.getString("method"));
			}
			if (json.has("off") && !json.isNull("off")) {
				entity.setOffset(json.getInt("off"));
			}
			if (json.has("len") && !json.isNull("len")) {
				entity.setLength(json.getInt("len"));
			}
			if (json.has("finished") && !json.isNull("finished")) {
				final String bool = json.getString("finished");
				boolean boolValue = !TextUtils.equals(bool, "N");

				if (boolValue) {
					entity.mParentEntity.setState(UploadEntity.STATE_ACCEPTED);
					entity.mParentEntity.setRemotePath(entity.getRequestPath());
				} else {
					// 如果是认为上传尚未完成，则不改变其状态。因为状态可能对应：Pending/Running/Canceled/
				}

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return entity;
	}
}
