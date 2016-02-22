package com.medzone.mcloud.measure;

import org.json.JSONException;
import org.json.JSONObject;

import com.medzone.mcloud.task.DoRuleMatchTask;
import com.medzone.framework.data.controller.AbstractController;
import com.medzone.framework.network.NetworkClientResult;
import com.medzone.framework.task.BaseResult;
import com.medzone.framework.task.ITaskCallback;
import com.medzone.framework.task.TaskHost;
import com.medzone.mcloud.data.bean.dbtable.BaseMeasureData;
import com.medzone.mcloud.data.bean.dbtable.BloodOxygen;
import com.medzone.mcloud.data.bean.dbtable.BloodPressure;
import com.medzone.mcloud.data.bean.dbtable.BloodSugar;
import com.medzone.mcloud.data.bean.dbtable.EarTemperature;
import com.medzone.mcloud.data.bean.dbtable.FetalHeart;
import com.medzone.mcloud.data.bean.dbtable.FetalMovement;
import com.medzone.mcloud.data.bean.dbtable.Rule;
import com.medzone.mcloud.errorcode.CloudStatusCodeProxy.LocalCode;

public class RuleController extends AbstractController<RuleCache> {

    private static RuleController controller;

    private RuleController() {
    }

    public static RuleController getInstance() {
        if (controller == null) {
            controller = new RuleController();
        }
        return controller;
    }

    @Override
    protected RuleCache createCache() {
        return new RuleCache();
    }

    public Rule getRulebyData(BaseMeasureData data) {
        return getCache().readRuleByData(data);
    }

    public void getAdvised(BaseMeasureData data, Boolean eval, final ITaskCallback callBack) {
        DoRuleMatchTask task = null;
        if (data == null) {
            return;
        }

        int accountID = data.getBelongAccount().getId();

        if (data.getBelongContactPerson() != null && data.getBelongContactPerson().getContactPersonID() != null) {
            accountID = data.getBelongContactPerson().getContactPersonID();
        }
        if (data instanceof BloodPressure) {
            BloodPressure bloodpressure = (BloodPressure) data;
            task = new DoRuleMatchTask(accountID, BloodPressure.TAG, Float.parseFloat(String.valueOf(bloodpressure.getHigh())), Float.parseFloat(String.valueOf(bloodpressure.getLow())),
                    Float.parseFloat(String.valueOf(bloodpressure.getRate())), null, null, null, eval);
        } else if (data instanceof BloodOxygen) {
            BloodOxygen oxy = (BloodOxygen) data;
            task = new DoRuleMatchTask(accountID, BloodOxygen.TAG_OXY, oxy.getOxygen().floatValue(), oxy.getRate().floatValue(), null, null, null, null, eval);
        } else if (data instanceof EarTemperature) {
            EarTemperature temperature = (EarTemperature) data;
            task = new DoRuleMatchTask(accountID, EarTemperature.TAG, temperature.getTemperature(), null, null, null, null, null, eval);
        } else if (data instanceof BloodSugar) {
            BloodSugar bloodSugar = (BloodSugar) data;
            task = new DoRuleMatchTask(accountID,
                    BloodSugar.TAG,
                    Float.parseFloat(bloodSugar.getSugarDisplay()),
                    null,
                    null,
                    null,
                    null,
                    bloodSugar.getMeasureState(),
//					RuleMatchUtil.convertEatState(CloudApplication.convertString(bloodSugar.getMeasureStateDisplay())),
                    eval,
                    bloodSugar.getAbnormal());
        } else if (data instanceof FetalHeart) {
            FetalHeart fh = (FetalHeart) data;
            task = new DoRuleMatchTask(accountID,
                    FetalHeart.TAG,
                    0f,
                    null,
                    null,
                    Float.valueOf(fh.getAvgFetal()),
                    fh.getMeasureDuration(),
                    null,
                    eval);

        } else if (data instanceof FetalMovement) {
            FetalMovement fm = (FetalMovement) data;
            task = new DoRuleMatchTask(accountID,
                    FetalMovement.TAG,
                    0f,
                    null,
                    null,
                    Float.valueOf(fm.getAvgFetal()),
                    fm.getMeasureDuration(),
                    null,
                    eval);
        }
        if (task == null) return;
        task.setTaskHost(new TaskHost() {
            @Override
            public void onPostExecute(int requestCode, BaseResult result) {
                super.onPostExecute(requestCode, result);
                if (callBack == null) return;
                switch (result.getErrorCode()) {
                    case LocalCode.CODE_SUCCESS:
                        NetworkClientResult temp = (NetworkClientResult) result;
                        JSONObject json = temp.getResponseResult();
                        String url = null;
                        try {
                            if (json.has("url") && !json.isNull("url")) {
                                url = json.getString("url");
                                callBack.onComplete(LocalCode.CODE_SUCCESS, url);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        callBack.onComplete(result.getErrorCode(), null);
                        break;
                }

            }
        });
        task.execute();
    }

    /**
     * 基于账号的问卷获取健康评估结果
     *
     * @param accountId 给定账号的id
     * @param callBack  回调事件，通常用户呈现返回的url
     */
    public void getHealthAssessment(int accountId, final ITaskCallback callBack) {
        DoRuleMatchTask task = null;
        task = new DoRuleMatchTask(accountId, "all", null, null, null, null, null, null, true);
        task.setTaskHost(new TaskHost() {
            @Override
            public void onPostExecute(int requestCode, BaseResult result) {
                super.onPostExecute(requestCode, result);
                if (callBack == null) return;
                String url = null;
                switch (result.getErrorCode()) {
                    case LocalCode.CODE_SUCCESS:
                        NetworkClientResult temp = (NetworkClientResult) result;
                        JSONObject json = temp.getResponseResult();
                        try {
                            if (json.has("url") && !json.isNull("url")) {
                                url = json.getString("url");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        break;
                }
                callBack.onComplete(result.getErrorCode(), url);
            }
        });
        task.execute();
    }
}
