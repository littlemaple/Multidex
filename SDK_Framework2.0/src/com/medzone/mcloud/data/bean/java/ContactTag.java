package com.medzone.mcloud.data.bean.java;

/**
 * @author Robert
 * @category 联系人首页[待同步标签,代测标签,标签管理标签]
 */
public class ContactTag {

	public ContactTag() {
	}

	private Integer	tagResID;

	private String	tagTitle;

	private Integer	tagOrder;

	public int getTagResID() {
		return tagResID;
	}

	public void setTagResID(int tagResID) {
		this.tagResID = tagResID;
	}

	public String getTagTitle() {
		return tagTitle;
	}

	public void setTagTitle(String tagTitle) {
		this.tagTitle = tagTitle;
	}

	public void setTagOrder(int tagOrder) {
		this.tagOrder = tagOrder;
	}

	public int getTagOrder() {
		return tagOrder;
	}

}
