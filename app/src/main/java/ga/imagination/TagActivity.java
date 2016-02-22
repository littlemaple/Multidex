package ga.imagination;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import ga.imagination.widget.SingleLineTag;


public class TagActivity extends Activity {
    SingleLineTag tagView;
    String[] result = new String[]{"", "B尿色清 酱油色 出汗多 服利尿剂 紧张 尿色清 酱油色 出汗多 服利尿剂 紧张", "尿色清 酱油色 出汗多 服利尿剂 紧张", "O尿色清 酱油色 出汗多 服利尿剂 紧张", "色清 酱油色 出汗多 服利尿剂 紧张", "MMM尿色清 酱油色 出汗多 服利--尿剂 紧张"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);
        tagView = (SingleLineTag) findViewById(R.id.tag_view);
        tagView.setTag(result[((int) (Math.random() * result.length))]);
    }

    public void change(View view) {
        tagView.setTag(result[((int) (Math.random() * result.length))]);
    }
}
