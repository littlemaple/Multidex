package com.medzone.mcloud.data.bean;

import java.util.ArrayList;
import java.util.List;

import com.medzone.framework.data.bean.BaseDatabaseObject;

public class IChatUtil {

	public static List<Long> convertInterlocutorId(String format) {

		String[] ret = format.split(",");
		List<Long> list = null;
		for (int i = 0; i < ret.length; i++) {
			if (list == null) list = new ArrayList<Long>();
			long item = Long.valueOf(ret[i]);
			if (item == BaseDatabaseObject.INVALID_ID) {
				list.add(null);
			}
			else {
				list.add(item);
			}

		}
		if (list.size() == 0) {
			throw new IllegalArgumentException(format + "$you input is un-received");
		}
		return list;
	}

}