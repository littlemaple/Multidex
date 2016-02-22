package ga.imagination;

import java.util.ArrayList;
import java.util.List;

import ga.imagination.lib.Tag;

/**
 * Created by 44260 on 2016/1/6.
 */
public class BaseModel {
    public String title;
    public String[] tags;

    public BaseModel(String title, String[] tags) {
        this.title = title;
        this.tags = tags;
    }

    public static List<BaseModel> create() {
        List<BaseModel> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(new BaseModel("[ pos:" + i + " ]", new String[]{"tag" + i, "tag1" + i, "tag1" + i, "tag1" + i}));
        }
        return list;
    }

    public Tag[] getTags() {
        Tag[] res = new Tag[tags.length];
        for (int i = 0; i < tags.length; i++) {
            res[i] = new Tag(i, tags[i]);
        }
        return res;
    }

}
