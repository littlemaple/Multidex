package imagination.ga.marsh;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import imagination.ga.account.AccountController;

/**
 * Created by 44260 on 2016/2/2.
 */
public class MarshActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout linearLayout = new LinearLayout(getBaseContext());
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        linearLayout.setGravity(Gravity.CENTER);
        TextView textView = new TextView(getBaseContext());
        textView.setBackgroundColor(Color.WHITE);
        textView.setTextColor(Color.BLACK);
        textView.setText(AccountController.getInstance().getCurrentAccount().toString());
        linearLayout.addView(textView);
        setContentView(linearLayout);
    }

    private void createFile() {
        String path = Environment.getExternalStorageDirectory() + "/marsh";
        File directory = new File(path);
        directory.mkdirs();
        File file = new File(path + "/marsh.txt");
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            bw.write(Build.MANUFACTURER + "," + Build.DEVICE);
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
