package com.medzone.mcloud.util;


public class UploadUtil {
	/**
	 * 重命名文件名，获取满足上传规范的文件名
	 * 
	 * @param type
	 *            测量类型，诸如：“bs”
	 * @param recordId
	 *            依附的测量数据rid
	 * @param size
	 *            文件字节数
	 * @param ext
	 *            文件扩展名
	 */
	public static String formatFileName(String type, long recordId, long size, String ext) {
		String c = "-";
		String dot = ".";
		return type + c + recordId + c + Integer.valueOf(String.valueOf(size), 16) + dot + ext;
	}

	public static String getFileSuffix(String path) {
		String[] arg = path.split("\\.");
		if (arg.length > 1) {
			return (arg[arg.length - 1]);
		}
		else {
			return null;
		}
	}

}
