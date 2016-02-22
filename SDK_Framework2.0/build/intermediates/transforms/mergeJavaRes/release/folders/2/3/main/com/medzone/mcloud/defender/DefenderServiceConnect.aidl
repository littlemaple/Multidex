package com.medzone.mcloud.defender;

import com.medzone.mcloud.defender.JPush;

interface DefenderServiceConnect{

	void initJPush();
	void startJPush();
	void stopJPush();
	String getRegisterID();
	boolean checkJPushConnectState(boolean isAutoConnecting);
	
}