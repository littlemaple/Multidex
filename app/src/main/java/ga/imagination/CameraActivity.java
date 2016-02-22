package ga.imagination;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.security.Permission;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by 44260 on 2016/2/1.
 */
public class CameraActivity extends AppCompatActivity {
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private Uri fileUri;
    private static final String IMAGE_PATH_SUFFIX = "ic.jpg";
    private static final String IMAGE_PATH_PREFIX = Environment.getExternalStorageDirectory() + "/medzone/selector";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout linearLayout = new LinearLayout(getBaseContext());
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        Button button = new Button(getBaseContext());
        button.setText("execute");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create Intent to take a picture and return control to the calling application
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                fileUri = getOutputMediaFileUri(); // create a file to save the image
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name
                // start the image capture Intent
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });
        Button btnCheckPermission = new Button(getBaseContext());
        btnCheckPermission.setText("check permission");
        btnCheckPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManager packageManager = getPackageManager();
                int res = packageManager.checkPermission("android.permission.CAMERA", "ga.imagination");
                Log.d(getClass().getSimpleName(), "permission:" + res);
                int checkRes = getApplicationContext().checkCallingOrSelfPermission("android.permission.CAMERA");
                Toast.makeText(getBaseContext(), "检查结果：" + checkRes, 1000).show();
                try {
                    PackageInfo pack = packageManager.getPackageInfo("ga.imagination", PackageManager.GET_PERMISSIONS);
                    String[] permissionStrings = pack.requestedPermissions;
//                    Toast.makeText(getBaseContext(), "权限清单--->" + Arrays.toString(permissionStrings), Toast.LENGTH_LONG).show();
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        linearLayout.addView(button);
        linearLayout.addView(btnCheckPermission);
        setContentView(linearLayout);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            // Image captured and saved to fileUri specified in the Intent
//            Toast.makeText(this, "Image saved to:\n" +
//                    data.getData(), Toast.LENGTH_LONG).show();
//        }
    }

    private String curPath;

    private Uri getOutputMediaFileUri() {
        File file = new File(IMAGE_PATH_PREFIX);
        file.mkdirs();
        String path = IMAGE_PATH_PREFIX + "/tp" + 5 + "_" + System.currentTimeMillis() + "" + IMAGE_PATH_SUFFIX;
        curPath = path;
        return Uri.fromFile(new File(path));
    }

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    /**
     * Create a file Uri for saving an image or video
     */
    private static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * Create a File for saving an image or video
     */
    private static File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }
}
