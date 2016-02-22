package com.medzone.mcloud.data.bean.java;


import android.util.Log;

import com.medzone.framework.Deploy;
import com.medzone.framework.util.MD5Util;
import com.medzone.mcloud.data.bean.AbstractRequestAssembler;
import com.medzone.mcloud.data.bean.RestParam;
import com.medzone.mcloud.data.bean.Sign;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by 44260 on 2016/2/4.
 */
public class UnifiedOrder extends AbstractRequestAssembler implements Sign {
    //公众账号 微信分配的公众账号ID（企业号corpid即为此appId）
    @RestParam(field_name = "appid", required = true)
    private String appId;
    //商户ID 微信支付分配的商户号
    @RestParam(field_name = "mch_id", required = true)
    private String mchId;
    //设备号 终端设备号(门店号或收银设备ID)，注意：PC网页或公众号内支付请传"WEB"
    @RestParam(field_name = "device_info", required = false)
    private String deviceInfo;
    //随机字符串，不长于32位。推荐随机数生成算法
    @RestParam(field_name = "nonce_str", required = true)
    private String nonceStr;
    //签名，详见签名生成算法
    @RestParam(field_name = "sign", required = true)
    private String sign;
    //商品描述 商品或支付单简要描述
    @RestParam(field_name = "body", required = true)
    private String body;
    //商品名称明细列表
    @RestParam(field_name = "detail")
    private String detail;
    //附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
    @RestParam(field_name = "attach")
    private String attach;
    //商户订单号 商户系统内部的订单号,32个字符内、可包含字母, 其他说明见商户订单号
    @RestParam(field_name = "out_trade_no", required = true)
    private String outTradeNo;
    //货币类型 符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
    @RestParam(field_name = "fee_type", required = false)
    private String feeType;
    //总金额	订单总金额，单位为分，详见支付金额
    @RestParam(field_name = "total_fee", required = true)
    private int totalFee;
    //终端Ip APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。
    @RestParam(field_name = "spbill_create_ip", required = true)
    private String spBillCreateIp;
    //交易起始时间 订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。其他详见时间规则
    @RestParam(field_name = "time_start")
    private String startTime;
    //订单失效时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010。其他详见时间规则
    //注意：最短失效时间间隔必须大于5分钟
    @RestParam(field_name = "time_expire")
    private String expireTime;
    //商品标记，代金券或立减优惠功能的参数，说明详见代金券或立减优惠
    @RestParam(field_name = "goods_tag")
    private String goodsTag;
    //通知地址 接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。
    @RestParam(field_name = "notify_url", required = true)
    private String notifyUrl;
    //交易类型 JSAPI--公众号支付、NATIVE--原生扫码支付、APP--app支付，统一下单接口trade_type的传参可参考这里
    //MICROPAY--刷卡支付，刷卡支付有单独的支付接口，不调用统一下单接口
    @RestParam(field_name = "trade_type", required = true)
    private String tradeType;
    //商品Id trade_type=NATIVE，此参数必传。此id为二维码中包含的商品ID，商户自行定义。
    @RestParam(field_name = "product_id")
    private String productId;
    //指定支付方式 no_credit--指定不能使用信用卡支付
    @RestParam(field_name = "limit_pay")
    private String limitPay;
    //用户标识 trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识
    @RestParam(field_name = "openid")
    private String openId;

    public String getAppId() {
        return appId;
    }

    public UnifiedOrder setAppId(String appId) {
        this.appId = appId;
        return this;
    }

    public String getMchId() {
        return mchId;
    }

    public UnifiedOrder setMchId(String mchId) {
        this.mchId = mchId;
        return this;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public UnifiedOrder setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
        return this;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public UnifiedOrder setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
        return this;
    }

    public String getSign() {
        return sign;
    }

    private UnifiedOrder setSign(String sign) {
        this.sign = sign;
        return this;
    }

    public String getBody() {
        return body;
    }

    public UnifiedOrder setBody(String body) {
        this.body = body;
        return this;
    }

    public String getDetail() {
        return detail;
    }

    public UnifiedOrder setDetail(String detail) {
        this.detail = detail;
        return this;
    }

    public String getAttach() {
        return attach;
    }

    public UnifiedOrder setAttach(String attach) {
        this.attach = attach;
        return this;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public UnifiedOrder setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
        return this;
    }

    public String getFeeType() {
        return feeType;
    }

    public UnifiedOrder setFeeType(String feeType) {
        this.feeType = feeType;
        return this;
    }

    public int getTotalFee() {
        return totalFee;
    }

    public UnifiedOrder setTotalFee(int totalFee) {
        this.totalFee = totalFee;
        return this;
    }

    public String getSpBillCreateIp() {
        return spBillCreateIp;
    }

    public UnifiedOrder setSpBillCreateIp(String spBillCreateIp) {
        this.spBillCreateIp = spBillCreateIp;
        return this;
    }

    public String getStartTime() {
        return startTime;
    }

    public UnifiedOrder setStartTime(String startTime) {
        this.startTime = startTime;
        return this;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public UnifiedOrder setExpireTime(String expireTime) {
        this.expireTime = expireTime;
        return this;
    }

    public String getGoodsTag() {
        return goodsTag;
    }

    public UnifiedOrder setGoodsTag(String goodsTag) {
        this.goodsTag = goodsTag;
        return this;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public UnifiedOrder setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
        return this;
    }

    public String getTradeType() {
        return tradeType;
    }

    public UnifiedOrder setTradeType(String tradeType) {
        this.tradeType = tradeType;
        return this;
    }

    public String getProductId() {
        return productId;
    }

    public UnifiedOrder setProductId(String productId) {
        this.productId = productId;
        return this;
    }

    public String getLimitPay() {
        return limitPay;
    }

    public UnifiedOrder setLimitPay(String limitPay) {
        this.limitPay = limitPay;
        return this;
    }

    public String getOpenId() {
        return openId;
    }

    public UnifiedOrder setOpenId(String openId) {
        this.openId = openId;
        return this;
    }

    public UnifiedOrder() {
    }

    @Override
    public void sign() {
        Field[] fields = getClass().getDeclaredFields();
        if (fields == null || fields.length == 0)
            return;
        HashMap<String, Object> result = new HashMap<>();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                RestParam unifiedOrder = field.getAnnotation(RestParam.class);
                if (unifiedOrder == null || "sign".equals(unifiedOrder.field_name()))
                    continue;
                Object object = field.get(this);
                boolean valid = (unifiedOrder.required() && object != null) || !unifiedOrder.required();
                if (!valid)
                    throw new IllegalArgumentException("be sure the " + unifiedOrder.field_name() + " not null");
                if (object == null)
                    continue;
                result.put(unifiedOrder.field_name(), object);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        setSign(createSign(result));
    }

    public static String createSign(HashMap<String, Object> params) {
        if (params == null || params.size() == 0)
            return null;
        TreeMap<String, Object> treeMap = new TreeMap<>(params);
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, Object> entry : treeMap.entrySet()) {
            if (entry.getValue() == null)
                continue;
            result.append(entry.getKey()).append("=").append(entry.getValue());
            result.append("&");
        }
        result.append("key=").append(Deploy.APP_KEY);
        Log.d(UnifiedOrder.class.getSimpleName(), "origin=>" + result.toString());
        Log.d(UnifiedOrder.class.getSimpleName(), "md5=>" + MD5Util.getMD5String(result.toString()).toUpperCase());
        return MD5Util.getMD5String(result.toString()).toUpperCase();
    }

    public enum TradeType {
        JSAPI(), NATIVE(), TradeType, APP()
    }

    @Override
    public HashMap<String, Object> assembleMap() {
        sign();
        return super.assembleMap();
    }
}
