package com.medzone.mcloud.data.bean.dbtable;

/**
 * Created by asusu on 2015/11/13.
 */
public class FactorItemBase<T> {

    public static final String NAME_FIELD_NAME =  "name";
    public static final String NAME_FIELD_VALUE = "value";
    public static final String NAME_FIELD_STATE = "state";


    public String name;
    public String cname;
    public T value;
    public Integer state = 0;
    public String unit;

}
