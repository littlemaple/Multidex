package ga.imagination;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import ga.imagination.databinding.TextLayoutBinding;
import ga.imagination.module.Doctor;
import ga.imagination.widget.DynamicListView;
import ga.imagination.widget.WrapperLinearLayoutManager;

/**
 * Created by 44260 on 2016/1/25.
 */
public class TextActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextLayoutBinding binding = DataBindingUtil.setContentView(this, R.layout.text_layout);
//        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
//        BaseTextAdapter adapter = new BaseTextAdapter();
//        adapter.setListener(new BaseTextAdapter.onItemClickListener() {
//            @Override
//            public void onClick(View view) {
////                recyclerView.requestLayout();
////                recyclerView.postInvalidate();
//            }
//        });
//        recyclerView.setAdapter(adapter);
//        LinearLayoutManager manager = new WrapperLinearLayoutManager(getBaseContext());
//        recyclerView.setLayoutManager(manager);
//        recyclerView.setNestedScrollingEnabled(false);
//        recyclerView.setHasFixedSize(false);

        binding.recycleView.setContent(Doctor.devObtain());
    }
}
