package ga.imagination;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.Map;

import ga.imagination.databinding.ContentSendBinding;
import ga.imagination.widget.SendMenuBar;
import imagination.ga.account.AccountController;
import imagination.ga.marsh.MarshActivity;

public class SendActivity extends AppCompatActivity {

    private static final String TAG = "Multi:imagination";
    private static final String KEY_DATA = "[{\"type\":\"bp\",\"typename\":\"血压\",\"result\":\"本周平均血压110.0\\/70.0mmHg,最高血压110\\/70mmHg,最低血压110\\/70mmHg，建议控制血压在90-140\\/60-90mmHg,超过预警高值0次，低于预警低值0次。\"},{\"type\":\"bs\",\"typename\":\"血糖\",\"result\":\"本周平均血糖6.7mmol\\/l,最高血糖11mmol\\/l,最低血糖2.5mmol\\/l，建议空腹血糖控制在4.4-7.0mmol\\/L，餐后血糖控制在4.4-10.0mmol\\/L，超过预警高值0次，低于预警低值1次。\"}]";

    private ObservableArrayList<String> content = new ObservableArrayList<>();
    private TagViewAdapter adapter;
    private ContentSendBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.content_send);
        binding.sendBar.registerParentView(getWindow().getDecorView());
        binding.setData(KEY_DATA);
        Glide.with(this).load("http://incrediblescene.cf/image/").transform(new GreyTransform(getBaseContext())).error(R.drawable.message_ic_camera).into(binding.imageView);
        binding.setContent(getString(R.string.str_tag));
        binding.sendBar.setOnMenuMonitorListener(new SendMenuBar.onMenuMonitorListener() {
            @Override
            public void onClickCamera(View view) {
                Glide.with(SendActivity.this).load("http://incrediblescene.cf/image/").transform(new GreyTransform(getBaseContext())).error(R.drawable.message_ic_camera).into(binding.imageView);
                if (binding.sendBar.getImageCount() >= 2) {
                    Snackbar.make(view, "最多只能上传2张图片", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                binding.sendBar.addImage(ContextCompat.getDrawable(getBaseContext(), R.drawable.message_ic_camera));
            }

            @Override
            public void onClickSend(View view, String text) {

            }
        });
//        setContentView(R.layout.content_tag);
//        TagView tagView = (TagView) findViewById(R.id.tag_view_main);
//        tagView.addTags(new String[]{"title", "title", "title", "title", "title"});
//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
//        adapter = new TagViewAdapter();
//        adapter.setList(BaseModel.create());
//        recyclerView.setAdapter(adapter);
//        final ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
//        setSupportActionBar(binding.toolbar);
//        binding.setInputContent(content);
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        content.add(0, "");
//        content.add(1, "");
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Editable editable = binding.content.et.getText();
//                String pwd = editable == null ? "" : editable.toString();
//                OkHttpClient client = new OkHttpClient();
//                client.newCall(new Request.Builder().url("http://192.168.0.1/api/log").post(new FormEncodingBuilder().add("log", "file").add("file", "fff").build()).build()).enqueue(new Callback() {
//                    @Override
//                    public void onFailure(Request request, IOException e) {
//                        Log.d(TAG, "onFailure");
//                        String res = request.toString();
//                        content.set(1, res);
//                    }
//
//                    @Override
//                    public void onResponse(Response response) throws IOException {
//
//                        String res = response.message() + "\n" + response.code() + "\n" + response.toString() + "\n" + response.body().string();
//                        content.set(0, res);
//                        Log.d(TAG, res);
//                    }
//                });
//            }
//        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.network) {
            startActivity(new Intent(this, NetWorkActivity.class));
            return true;
        }
        if (id == R.id.camera) {
            startActivity(new Intent(this, CameraActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void AddTail(View view) {
        adapter.addTag(new BaseModel("new", new String[]{"head", "ttttt"}));
    }

    public void AddHead(View view) {
//        adapter.addTag(0, new BaseModel("new", new String[]{"tail", "ttttt"}));
        startActivity(new Intent(this, SecondActivity.class));
    }


    public void Post(View view) {
        startActivity(new Intent(this, MarshActivity.class));
        Volley.newRequestQueue(this).add(new StringRequest(Request.Method.POST, "http://incrediblescene.cf/api/log/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "response:" + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error == null ? "null" : "error:" + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("log", "result");
                return map;
            }
        });

    }

    public void onTag(View view) {
        startActivity(new Intent(this, TagActivity.class));
    }

    public void Net(View view) {
        startActivity(new Intent(this, NetWorkActivity.class));
    }

    public void ClickLines(View view) {
        TextView textView = (TextView) view;
        textView.setMaxLines(isLineSet ? 2 : Integer.MAX_VALUE);
        isLineSet = !isLineSet;
    }

    private boolean isLineSet;

    public void set(View view) {
        AccountController.getInstance().getCurrentAccount().setAccountName("accountName");
        AccountController.getInstance().getCurrentAccount().setId((int) (Math.random()*100));
    }
}
