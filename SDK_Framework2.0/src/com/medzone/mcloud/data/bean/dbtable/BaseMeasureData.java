package com.medzone.mcloud.data.bean.dbtable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import com.j256.ormlite.field.DatabaseField;
import com.medzone.framework.data.bean.Account;
import com.medzone.framework.data.bean.BasePagingContent;

import com.medzone.mcloud.data.bean.IMeasureLocation;
import com.medzone.mcloud.data.bean.IRuleDetails;
import com.medzone.framework.data.IRuleRes;
import com.medzone.mcloud.util.BaseMeasureDataUtil;

/**
 * 
 * @author Robert.
 * 
 */
public abstract class BaseMeasureData extends BasePagingContent<String> implements IRuleRes,IRuleDetails, IMeasureLocation, Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID							= 4712210551546507844L;
	public static final int		ACTION_ADD_RECORD							= ACTION_NORMAL + 1;			// 新增记录
	public static final int		ACTION_ADD_RECORD_INCOMPLETE				= ACTION_NORMAL + 2;			// 新增记录,写入记录未完成状态,通常用于大文件或长期数据的写入
	public static final int		ACTION_UPDATE_RECORD						= ACTION_NORMAL + 101;			// 更新记录,API也未考虑同步更新的情况,则客户端不进行考虑
	public static final int		ACTION_DELETE_RECORD						= ACTION_NORMAL + 201;			// 删除记录
	public static final int		ACTION_DELETE_RECORD_ATTACHMENT				= ACTION_NORMAL + 202;			// 删除记录附件

	public static final String	FIELD_FOREIGN_NAME_MASTER_CONTACT_PERSON_ID	= "master_contact_person_id";

	public static final String	NAME_FIELD_MEASURETIME_HELP					= "measureTimeHelp";
	public static final String	NAME_FIELD_MEASURETIME						= "measureTime";
	public static final String	NAME_FIELD_MEASUREU_ID						= "measureUID";
	public static final String	NAME_FIELD_RECORD_ID						= "recordID";
	public static final String	NAME_FIELD_README							= "readme";
	public static final String	NAME_FIELD_SOURCE							= "source";
	public static final String	NAME_FIELD_X								= "x";
	public static final String	NAME_FIELD_Y								= "y";
	public static final String	NAME_FIELD_Z								= "z";
	public static final String	NAME_FIELD_ABNORMAL							= "abnormal";
	public static final String	NAME_FIELD_DATA_CREATE_ID					= "dataCreateID";
	public static final String	NAME_FIELD_IS_TEST_CREATE_DATA				= "isTestCreateData";

	// ------------------------------------其他属性-----------------------------------------------
	protected List<Rule>		allRules;

	// ------------------------------------库表字段-----------------------------------------------

	@DatabaseField(foreign = true, columnName = FIELD_FOREIGN_NAME_MASTER_CONTACT_PERSON_ID, foreignAutoRefresh = true)
	private ContactPerson		belongContactPerson;														// 最终这条数据的归属,仅在代测情况下被赋值

	@DatabaseField(columnName = NAME_FIELD_MEASURETIME)
	private Long				measureTime;

	@DatabaseField(columnName = NAME_FIELD_MEASUREU_ID)
	private String				measureUID;

	@DatabaseField(columnName = NAME_FIELD_MEASURETIME_HELP)
	private String				measureTimeHelp;

	@DatabaseField(columnName = NAME_FIELD_README)
	private String				readme;

	@DatabaseField(columnName = NAME_FIELD_RECORD_ID)
	private Integer				recordID;

	@DatabaseField(columnName = NAME_FIELD_SOURCE)
	private String				source;

	@DatabaseField(columnName = NAME_FIELD_X)
	private Double				x;

	@DatabaseField(columnName = NAME_FIELD_Y)
	private Double				y;

	@DatabaseField(columnName = NAME_FIELD_Z)
	private Double				z;

	@DatabaseField(columnName = NAME_FIELD_ABNORMAL)
	private Integer				abnormal;

	@DatabaseField(columnName = NAME_FIELD_DATA_CREATE_ID)
	private Integer				dataCreateID;																// 数据创建ID，通常都是自己,云端返回

	@DatabaseField(defaultValue = "0", columnName = NAME_FIELD_IS_TEST_CREATE_DATA)
	private Boolean				isTestCreateData;															// 是否是代测产生的数据

	// ------------------------------------------------Setter/Getter-------------------------

	/**
	 * 设置测量数据归属时，如果设置为他人的数据。则不能够设置为自己的数据。
	 * 
	 * @see {@link #setBelongAccount(Account)}
	 */
	public void setBelongContactPerson(ContactPerson belongContactPerson) {
		this.belongContactPerson = belongContactPerson;
	}

	public ContactPerson getBelongContactPerson() {
		return belongContactPerson;
	}

	public Long getMeasureTime() {
		return this.measureTime;
	}

	/**
	 * 解决measureTime与MeasureUid解析出来的时间不一致的问题。
	 */
	private void forceConvertMeasureTime() {
		if (this.measureUID != null) {
			this.measureTime = BaseMeasureDataUtil.parseUID2Millisecond(measureUID);
			this.measureTimeHelp = BaseMeasureDataUtil.parseUID2DateString(measureUID);
		}
	}

	public String getMeasureTimeHelp() {
		return measureTimeHelp;
	}

	public String getMeasureUID() {
		return measureUID;
	}

	public void setMeasureUID(String measureUID) {
		this.measureUID = measureUID;
		forceConvertMeasureTime();
	}

	public String getReadme() {
		return readme;
	}

	public void setReadme(String readme) {
		this.readme = readme;
	}

	public Integer getRecordID() {
		return recordID;
	}

	public void setRecordID(Integer recordID) {
		this.recordID = recordID;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Double getX() {
		return x;
	}

	public void setX(Double x) {
		this.x = x;
	}

	public Double getY() {
		return y;
	}

	public void setY(Double y) {
		this.y = y;
	}

	public Double getZ() {
		return z;
	}

	public void setZ(Double z) {
		this.z = z;
	}

	public Integer getAbnormal() {
		return abnormal;
	}

	public void setAbnormal(Integer abnormal) {
		this.abnormal = abnormal;
	}

	public Integer getDataCreateID() {
		return dataCreateID;
	}

	/**
	 * 数据是由谁创建的，通常情况下均为自己。
	 * 
	 * @param dataCreateID
	 */
	public void setDataCreateID(Integer dataCreateID) {
		this.dataCreateID = dataCreateID;
	}

	public boolean isTestCreateData() {
		return isTestCreateData;
	}

	public void setTestCreateData(boolean isTestCreateData) {
		this.isTestCreateData = isTestCreateData;
	}

	/**
	 * 根据规则判断测量数据是否为健康状态，一般情况下健康含理想，正常。
	 * 
	 * @return boolean
	 */
	public abstract boolean isHealthState();

	public abstract String getMeasureName();

	public boolean isSameRecord(Object o) {
		BaseMeasureData item2 = (BaseMeasureData) o;
		if (!this.getMeasureUID().equals(item2.getMeasureUID())) {
			return false;
		}
		return true;
	}

	public void setLocation(HashMap<String, ?> map) {
		// HashMap<String, ?> map =
		// CloudLocationClient.getInstance().getLocationParams();
		if (map != null) {
			this.setX(Double.valueOf(map.get(IMeasureLocation.LOCATION_LONTITUDE).toString()));
			this.setY(Double.valueOf(map.get(IMeasureLocation.LOCATION_LATITUDE).toString()));
			this.setZ(Double.valueOf(map.get(IMeasureLocation.LOCATION_ALTITUDE).toString()));
			// this.setAddress(""+map.get(IMeasureLocation.LOCATION_ADDRESS));
		}
	}
}
