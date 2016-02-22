package com.medzone.mcloud.lbs;

import android.location.Location;

public class CouldLocation extends Location {

	private int		code;
	private String	address;

	public CouldLocation(String provider) {
		super(provider);
		// TODO Auto-generated constructor stub
	}

	public CouldLocation(Location l) {
		super(l);
	}

	public void setLbsCode(int code) {

		this.code = code;
	}

	public void setLbsAddress(String addr) {

		this.address = addr;
	}

	public int getLbsCode() {

		return this.code;
	}

	public String setLbsAddress() {

		return this.address;
	}

}
