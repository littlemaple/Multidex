package com.medzone.mcloud.data.bean.java;

import android.util.Log;
import android.util.SparseArray;

import com.medzone.mcloud.util.IOUtils;

import java.util.ArrayList;


public class Event {
	public int timeStamp;
	public int type;
	public byte feel; 
	
	public long timeOccur;
	public int  typeOccur;
	public static final boolean bigEnding = true;
	public static final int inSecond = 1;
	public static final int EVT_POS  = 2 + inSecond;
	public static final int FEEL_POS = 6 + inSecond;
	public static final int EVT_LEN  = 7 + inSecond;
	
	static public final int A = 0x00000002;//0x00000002   停搏
	static public final int B = 0x00000001;//0x00000001   室颤
	static public final int C = 0x00080000;//0x00080000   室速
	static public final int D = 0x00800000;//0x00800000	室性节律
	static public final int E = 0x40000000;//0x40000000   室性二连发
	static public final int F = 0x80000000;//0x80000000   室性多连发
	static public final int G = 0x00400000;//0x00400000	室性二联律
	static public final int H = 0x00200000;//0x00200000   室性三联律
	static public final int I = 0x20000000;//0x20000000  R on T
	static public final int J = 0x10000000;//0x10000000   不规则节律
	static public final int K = 0x04000000;//0x04000000	心动过速
	static public final int L = 0x02000000;//0x02000000	心动过缓
	static public final int M = 0x00000020;//0x00000020   漏博
	
	public static final int FEEL_NONE   = 0;
	public static final int FEEL_HEAD   = 1; //胸闷
	public static final int FEEL_BREAST = 2; //头晕
	public static final int FEEL_HEART  = 3; //心慌
	public static final int FEEL_USER   = 4; //其他
	
//	public static final int FEEL_OTHER_1_ID = 1<<2;
//	public static final int FEEL_OTHER_2_ID = 2<<2;
//	public static final int FEEL_OTHER_3_ID = 3<<2;
//	public static final int FEEL_OTHER_4_ID = 4<<2;
//	public static final int FEEL_OTHER_5_ID = 5<<2;
//  ...
//	public static final int FEEL_OTHER_63_ID = 63<<2;

	public static final int[] ALL_TYPES = {A, B, C, D, E, F, G, H, I, J, K, L, M};
	public static final int[] WARNING_TYPES = {A, B, C, K, L};
	public static final int[] EVENT_TYPES = {D, E, F, G, H, I, J, M};
	static public final String DISCRIPTION[] = {"停搏","室颤","室速","室性节律","室性二连发","室性多连发","室早二联律","室早三联律","R-on-T","不规则节律","心动过速","心动过缓","漏搏"};
	static public final String DISCRIPTION_EN[] = {"ASYSTOLE","FIBRILLATION","VENTRICULAR TACH","VENTRICULAR RHYTHM","PAIR PVC","Multiple PVC","BIGEMINY","TRIGEMINY","R-on-T","unregular beat","beat too fast","beat too slow","MISSED BEAT"};
	
	static public final String FEEL_DISCRIPTION[] = { "胸闷", "胸痛",  "心悸", "头晕", "头痛","其他"};
	public static SparseArray<String> otherFeelings= new SparseArray<String>(); //other feelings 1-63

	public static Event read(byte[] params, int offset) {
		if ((params.length < 4)
				|| (params.length > 4 && (params.length - offset) < 4))
			return null;

		Event data = new Event();
		data.timeStamp = IOUtils.byteArrayToShort(params, offset) << 16;
		offset += EVT_POS;
		data.type = IOUtils.byteArrayToInt(params, offset);
		return data;
	}

	public static Event[] readArray(byte[] params, int offset,
			int dataLen) {
		
		if ((params.length < dataLen)
				|| (params.length > dataLen && (params.length - offset) < dataLen))
			return null;

		int size = dataLen / EVT_LEN;
		int size2= size;
		for (int j = size -1; j >=0; j--, size2--){
			boolean allZero = true;
			for (int k = 0; k<EVT_LEN; k++){
				if (params[offset + j * EVT_LEN + k] != 0 && params[offset + j * EVT_LEN + k] != -1){
					allZero = false;
					break;
				}
			}
			if (!allZero)
				break;
		}
		Event[] datas = new Event[size2];
		for (int i = 0; i < size2; i++) {
			int current = offset + i * EVT_LEN;
			datas[i] = new Event();
			if(bigEnding){
				datas[i].timeStamp = IOUtils.byteArrayToShort(params, current) << 16;
				datas[i].type = IOUtils.byteArrayToInt(params, current+EVT_POS);
			}
			else{
				datas[i].timeStamp = IOUtils.byteArrayToShort2(params, current) << 16;
				datas[i].type = IOUtils.byteArrayToInt2(params, current+EVT_POS);
			}
			
			datas[i].feel = params[current + FEEL_POS]; //TODO: new protocal
		}
		
		return datas;
	}
	
	public static byte[] write(Event obj) {
		byte[] data = new byte[EVT_LEN];
		IOUtils.shortToByteArray((short)(obj.timeStamp>>16), data, 0);
		IOUtils.intToByteArray(obj.type, data, 3);
		data[7] = obj.feel;
		return data;
	}
	
	public static byte[] writeSparseArray(SparseArray<Event> eventList) {
		int len = eventList.size();
		byte[] events = new byte[EVT_LEN * len];
		for (int i = 0; i < len; i++) {
			Event evt = eventList.valueAt(i);
			if(bigEnding){
				events[i * EVT_LEN + 0]         = (byte) (evt.timeStamp >> 24);
				events[i * EVT_LEN + 1]         = (byte) (evt.timeStamp >> 16);
				events[i * EVT_LEN + EVT_POS]   = (byte) (evt.type >> 24);
				events[i * EVT_LEN + EVT_POS+1] = (byte) (evt.type >> 16);
				events[i * EVT_LEN + EVT_POS+2] = (byte) (evt.type >> 8);
				events[i * EVT_LEN + EVT_POS+3] = (byte) (evt.type );
				events[i * EVT_LEN + FEEL_POS]  = (byte) (evt.feel);
			}
			else{
				events[i * EVT_LEN + 0]         = (byte) (evt.timeStamp >> 16);
				events[i * EVT_LEN + 1]         = (byte) (evt.timeStamp >> 24);
				events[i * EVT_LEN + EVT_POS]   = (byte) (evt.type );
				events[i * EVT_LEN + EVT_POS+1] = (byte) (evt.type >>  8);
				events[i * EVT_LEN + EVT_POS+2] = (byte) (evt.type >> 16);
				events[i * EVT_LEN + EVT_POS+3] = (byte) (evt.type >> 24);
				events[i * EVT_LEN + FEEL_POS]  = (byte) (evt.feel);
			}
			
		}
		Log.v("Event", "<<>>#event real len= " + eventList.size());
		return events;
	}

	public static byte[] writeArray(Event[] objs) {
		byte events[] = new byte[objs.length * EVT_LEN];
		for (int i = 0; i < objs.length; i++) {
			Event evt = objs[i];
			if(bigEnding){
				events[i * EVT_LEN + 0]         = (byte) (evt.timeStamp >> 24);
				events[i * EVT_LEN + 1]         = (byte) (evt.timeStamp >> 16);
				events[i * EVT_LEN + EVT_POS]   = (byte) (evt.type >> 24);
				events[i * EVT_LEN + EVT_POS+1] = (byte) (evt.type >> 16);
				events[i * EVT_LEN + EVT_POS+2] = (byte) (evt.type >> 8);
				events[i * EVT_LEN + EVT_POS+3] = (byte) (evt.type );
				events[i * EVT_LEN + FEEL_POS]  = (byte) (evt.feel);
			}
			else{
				events[i * EVT_LEN + 0]         = (byte) (evt.timeStamp >> 16);
				events[i * EVT_LEN + 1]         = (byte) (evt.timeStamp >> 24);
				events[i * EVT_LEN + EVT_POS]   = (byte) (evt.type );
				events[i * EVT_LEN + EVT_POS+1] = (byte) (evt.type >>  8);
				events[i * EVT_LEN + EVT_POS+2] = (byte) (evt.type >> 16);
				events[i * EVT_LEN + EVT_POS+3] = (byte) (evt.type >> 24);
				events[i * EVT_LEN + FEEL_POS]  = (byte) (evt.feel);
			}
		}
		return events;
	}
	
	public static short[] statisticArray(byte[] eventList, int len){
		short[] result = new short[18];
		int eventLen = len / EVT_LEN;
		for (int i = 0; i < eventLen; i++) {
			int event = 0;
			if(bigEnding){
				event = IOUtils.byteArrayToInt(eventList, i * EVT_LEN + EVT_POS);
			}
			else{
				event = IOUtils.byteArrayToInt2(eventList, i * EVT_LEN + EVT_POS);
			}
			for (int j = 0; j < Event.ALL_TYPES.length; j++) {
				if ((event & Event.ALL_TYPES[j]) != 0) {
					result[j]++;
				}
			}
			byte feel = eventList[i * EVT_LEN + FEEL_POS];
			if (feel > 0) {
				if (feel > 5)
					feel = 5;
				result[12 + feel]++;
			}
		}
		return result;
	}
	
	public boolean hasFeel(){
		return feel != 0;
	}
	
	public static boolean isWarning(int type)
	{
		for (int i=0; i<WARNING_TYPES.length; i++){
			if ((type & WARNING_TYPES[i]) != 0) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		StringBuffer text = new StringBuffer();
		//text.append("时间        " + MyTimeStamp.toString(timeStamp) + "\r\n");
//		byte [] displayBytes = IOUtils.intToByteArray(type);
//		text.append(IOUtils.cmdToString(displayBytes, 4) + "\r\n");
		for (int i=0; i<ALL_TYPES.length; i++){
			if ((type & ALL_TYPES[i]) != 0) {
				text.append(DISCRIPTION[i] + " ");
			}	
		}
		
		if (feel > 0 && feel < 7) {
			text.append(FEEL_DISCRIPTION[feel-1] + " ");
		}
		else if ((feel & 0x07) == 7){
			int feelID = feel >> 3;
			text.append("unknown");
		}
		
		
		return text.toString();
	}
	
	public String toEnglishString(){
		StringBuffer text = new StringBuffer();
		for (int i=0; i<ALL_TYPES.length; i++){
			if ((type & ALL_TYPES[i]) != 0)
				text.append(DISCRIPTION_EN[i] + ", ");
		}
		return text.toString();
	}
	
	//获得警告事件类型
	public static ArrayList<Integer> getWarningTypeList(){
		ArrayList<Integer> typeList = new ArrayList<Integer>();
		for (int i=0; i<WARNING_TYPES.length; i++){
			typeList.add(WARNING_TYPES[i]);		
		}
		return typeList;
	}
	
	//获得失常事件类型
	public static ArrayList<Integer> getAbnormalTypeList(){
		ArrayList<Integer> typeList = new ArrayList<Integer>();
		for (int i=0; i<EVENT_TYPES.length; i++){
			typeList.add(EVENT_TYPES[i]);
		}
						
		return typeList;
	}
	
	//返回事件基本类型列表
	public static ArrayList<Integer> getTypeList(){
		ArrayList<Integer> typeList = new ArrayList<Integer>();
		for (int i=0; i<ALL_TYPES.length; i++){
			typeList.add(ALL_TYPES[i]);	
		}		
		return typeList;
	}
	
	//返回type中所包含的事件：
	public static ArrayList<Integer> getTypeOccurList(int type){
		ArrayList<Integer> typeOccurList = new ArrayList<Integer>();
		for (int i=0; i<ALL_TYPES.length; i++){
			if ((type & ALL_TYPES[i]) != 0) {
				typeOccurList.add(ALL_TYPES[i]);
			}
		}
		return typeOccurList;
	}
}
