package ga.imagination.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by 44260 on 2016/1/12.
 */
public class JsonPanel extends LinearLayout {
    public JsonPanel(Context context) {
        this(context, null);
    }

    public JsonPanel(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JsonPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
    }


    public void setJsonData(String data) {
        try {
            JSONArray jsonArray = new JSONArray(data);
            removeAllViews();
            List<Item> list = Item.create(jsonArray);
            for (Item item : list) {
                TextView textView = new TextView(getContext());
                textView.setText(item.typeName);
                textView.setPadding(0, 5, 0, 5);
                addView(textView);
                View view = new View(getContext());
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, 2);
                view.setBackgroundColor(Color.parseColor("#dcdcdc"));
                view.setLayoutParams(params);
                addView(view);
                TextView textView1 = new TextView(getContext());
                textView1.setText(item.result);
                textView1.setPadding(0, 5, 0, 5);
                addView(textView1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static class Item {
        String type;
        String typeName;
        String result;

        private Item(String type, String typeName, String result) {
            this.type = type;
            this.typeName = typeName;
            this.result = result;
        }

        private Item() {
        }

        static Item parse(JSONObject jsonObject) {
            Item item = new Item();
            try {
                if (jsonObject.has("type") && !jsonObject.isNull("type")) {
                    item.type = jsonObject.getString("type");
                }
                if (jsonObject.has("typename") && !jsonObject.isNull("typename")) {
                    item.typeName = jsonObject.getString("typename");
                }
                if (jsonObject.has("result") && !jsonObject.isNull("result")) {
                    item.result = jsonObject.getString("result");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return item;
        }

        static List<Item> create(JSONArray jsonArray) throws JSONException {
            List<Item> list = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++)
                list.add(parse(jsonArray.getJSONObject(i)));
            return list;
        }


    }

}
