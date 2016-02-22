package ga.imagination;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;

import ga.imagination.databinding.ActivityImageBinding;
import ga.imagination.widget.Util;

/**
 * Created by 44260 on 2016/1/14.
 */
public class SecondActivity extends Activity {

    private ActivityImageBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_image);
        Uri uri = Util.createUriFromDrawable(this, R.drawable.message_ic_camera);
        binding.setData(uri.toString() + "," + "http://imagination.ga/image/" + "," + "http://imagination.ga/image/" + "," + "http://imagination.ga/image/");
    }

    public void removeFirst(View view) {
        binding.imageGallery.removeFirst();
    }

    public void remove2(View view) {
        binding.imageGallery.remove(2);
    }

    public void removeLast(View view) {
        binding.imageGallery.removeLast();
    }

    public void addImage(View view) {
        binding.imageGallery.addImage("http://imagination.ga/image/", "imagina");
    }

    public void addTextView(View view) {
        binding.imageGallery.addTextView("照片", ContextCompat.getDrawable(getBaseContext(), R.drawable.service_consult_ic_camera), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "点击了照片", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void addTextViewLast(View view) {
        binding.imageGallery.addTextView("照片", ContextCompat.getDrawable(getBaseContext(), R.drawable.service_consult_ic_camera), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "点击了照片", Snackbar.LENGTH_SHORT).show();
            }
        }, true);
    }

    public void getTags(View view) {
        binding.tvDisplay.setText(binding.imageGallery.getImageTags());
    }

    @Override
    protected void onDestroy() {
        binding.imageGallery.unInit();
        super.onDestroy();
    }
}
