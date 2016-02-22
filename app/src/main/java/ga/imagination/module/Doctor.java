package ga.imagination.module;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.NonNull;

import ga.imagination.BR;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 44260 on 2015/12/17.
 */
public class Doctor extends BaseObservable implements Serializable {


    private int id;
    private String name;
    private String avatar;
    private String description;
    private String title;
    private String specialty;

    @Bindable
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        notifyPropertyChanged(BR.id);
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
        notifyPropertyChanged(BR.avatar);
    }

    @Bindable
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        notifyPropertyChanged(BR.description);
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    @Bindable
    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
        notifyPropertyChanged(BR.specialty);
    }

    private static Doctor parse(JSONObject jsonObject) {
        return parse(jsonObject, null);
    }

    public static Doctor parse(JSONObject jsonObject, Doctor item) {
        if (item == null)
            item = new Doctor();
        try {
            if (jsonObject.has("doctorid") && !jsonObject.isNull("doctorid")) {
                item.setId(jsonObject.getInt("doctorid"));
            }
            if (jsonObject.has("avatar") && !jsonObject.isNull("avatar")) {
                item.setAvatar(jsonObject.getString("avatar"));
            }
            if (jsonObject.has("description") && !jsonObject.isNull("description")) {
                item.setDescription(jsonObject.getString("description"));
            }
            if (jsonObject.has("title") && !jsonObject.isNull("title")) {
                item.setTitle(jsonObject.getString("title"));
            }
            if (jsonObject.has("specialty") && !jsonObject.isNull("specialty")) {
                item.setSpecialty(jsonObject.getString("specialty"));
            }
            if (jsonObject.has("name") && !jsonObject.isNull("name")) {
                item.setName(jsonObject.getString("name"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return item;
    }

    public static List<Doctor> create(@NonNull JSONArray jsonArray) {
        List<Doctor> list = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                list.add(parse(jsonArray.getJSONObject(i)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<Doctor> devObtain() {
        try {
            JSONArray jsonArray = new JSONArray(data);
            return create(jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static String data = "[{\"title\":\"教授\",\"doctorid\":8,\"avatar\":\"http:\\/\\/dev.mcloudlife.com\\/userimages\\/doctor_1453182052.png?1453182046\",\"specialty\":\"擅长某某某擅长\",\"description\":\"简介和哈哈哈哈哈哈哈哈哈\",\"name\":\"王新\"},{\"title\":\"教授、主任医师、博士生导师\",\"doctorid\":13,\"avatar\":\"http:\\/\\/dev.mcloudlife.com\\/userimages\\/doctor_1452648548.png?1452648532\",\"specialty\":\"这种策略的特征是在自己处于市场领导者地位时，可以主动降低价格，压缩竞争对手的生存空间，从而保证自己的优势地位。当自己在较大市场份额盈利时，竞争对手由于市场份额少，所以盈利也少，甚至由于规模不经济而无法盈利\",\"description\":\"享受国务院特殊津贴，入选全国“百千万人才工程”第一、二层次人员，获 美国NKF“国际卓越成就”奖章。担任中华医学会肾脏病学分会副主任委员、卫生部中国肾移植科学登记系统管理委员会副主任委员兼秘书长、浙江省医学会肾脏病学分会主任委员、器官移植学分会副主任委员等学术职务。主持国家及省部级科研项目50余项。获国家科技进步二等奖2项，浙江省科技进步一等奖6项，浙江省科技进步二等奖3项。发表学术论文310篇，其中SCI收录论文129篇。\",\"name\":\"苏素1\"},{\"title\":\"副主任医师、硕士生导师\",\"doctorid\":16,\"avatar\":\"http:\\/\\/dev.mcloudlife.com\\/userimages\\/doctor_1452648309.png?1452648442\",\"specialty\":\"长期从事肾脏移植血液净化肾脏病临床工作对肾移植术前配型工作移植手术及肾移植术后管理有丰富的临床经验长期从事肾脏移植血液净化肾脏病临床工作对肾移植术前配型工作肾移植手术及肾移植术后管理有丰富的临床经验长长期从事肾脏移植血液净化肾脏病临床工作对肾移植术前配型工作移植手术及肾移植术后管理有丰富的临床经验长期从事肾脏移植血液净化肾脏病临床工作对肾移植术前配型工作肾移植手术及肾移植术后管理有丰富的临床经验长长期从事肾脏移植血液净化肾脏病临床工作对肾移植术前配型工作移植手术及肾移植术后管理有丰富的临床经验长期从事肾脏移植血液净化肾脏病临床工作对肾移植术前配型工作肾移植手术及肾移植术后管理有丰富的临床经验长长期从事肾脏移植血液净化肾脏病临床工作对肾移植术前配型工作移植手术及肾移植术后管理有丰富的临床经验长期从事肾脏移植血液净化肾脏病临床工作对肾移植术前配型工作肾移植手术及肾移植术后管理有丰富的临床经验长长期从事肾脏移植血液净化肾脏病临床工作对肾移植术前配型工作移植手术及肾移植术后管理有丰富的临床经验长期从事肾脏移植血液净化肾脏病临床工作对肾移植术前配型工作肾移植手术及肾移植术后管理有丰富的临床经验长\",\"description\":\"享受国务院特殊津贴，入选全国“百千万人才工程”第一、二层次人员，获 美国NKF“国际卓越成就”奖章。担任中华医学会肾脏病学分会副主任委员、卫生部中国肾移植科学登记系统管理委员会副主任委员兼秘书长、浙江省医学会肾脏病学分会主任委员、器官移植学分会副主任委员等学术职务。主持国家及省部级科研项目50余项。获国家科技进步二等奖2项，浙江省科技进步一等奖6项，浙江省科技进步二等奖3项。发表学术论文310篇，其中SCI收录论文129篇。\",\"name\":\"苏素2\"},{\"title\":\"\",\"doctorid\":29,\"avatar\":\"http:\\/\\/dev.mcloudlife.com\\/img\\/avatar\\/unknown.png\",\"specialty\":\"什么都擅长\",\"description\":\"简介\",\"name\":\"柳\"}]";
}
