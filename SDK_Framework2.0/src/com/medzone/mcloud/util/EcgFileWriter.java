package com.medzone.mcloud.util;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Date;

public class EcgFileWriter  {
	public static final String ECG_FILE_PATH = getPath();
	private File mFile = null;
	private PrintWriter mFileWriter = null;

	public void openFile(String filePathAndName) {
		try {
			String filePath = filePathAndName;
			mFile = new File(filePath);
			if (!mFile.exists()) {
				mFile.createNewFile();
				System.out.println("�����ļ��ɹ�");
			}
			FileWriter resultFile = new FileWriter(mFile);
			mFileWriter = new PrintWriter(resultFile);
		} catch (Exception e) {
			System.out.println("�½��ļ���������");
			e.printStackTrace();
		}
	}
	
	private static String getPath(){
		Date date = new Date();
		String path = android.os.Environment.getExternalStorageDirectory()+ "/ecg_"+date.getMonth()+"_"+date.getDate()+"_"+date.getHours()+"_"+date.getMinutes()+".txt";
		return path;
	}

	public void writeFile(String content) {
		if (mFileWriter == null) {
			openFile(ECG_FILE_PATH);
			if (mFileWriter == null)
				return;
		}
		mFileWriter.print(content);
		mFileWriter.flush();
	}
}
