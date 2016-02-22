package ga.imagination;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import ga.imagination.databinding.ActivityMainBinding;
import imagination.ga.marsh.PayActivity;

/**
 * Created by 44260 on 2016/2/3.
 */
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(binding.toolbar);
        startActivity(new Intent(this, GraduatedScaleActivity.class));
        binding.toolbar.setSubtitleTextColor(Color.WHITE);
        binding.toolbar.setTitleTextColor(Color.WHITE);
        binding.toolbar.setTitle("");
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
        if (id == R.id.pay) {
            startActivity(new Intent(this, PayActivity.class));
            return true;
        }
        if (id == R.id.graduatedscale) {
            startActivity(new Intent(this, GraduatedScaleActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
