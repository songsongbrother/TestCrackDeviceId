package com.song.testcrackdeviceid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ishumei.smantifraud.SmAntiFraud;

/**
 * Created by chensongsong on 2019/9/9.
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 获取数美设备指纹
        findViewById(R.id.smid).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SmAntiFraud.SmOption option = new SmAntiFraud.SmOption();
                // organization 代码 不要传 AccessKey
                String DEBUG_ORG = "sRZ5IcWGlWLe5mCObbI7";
                option.setOrganization(DEBUG_ORG);
                option.setChannel("test");
                SmAntiFraud.create(getApplicationContext(), option);

                String deviceId = SmAntiFraud.getDeviceId();
//
//                Toast.makeText(getApplicationContext(), "smid: " + deviceId, Toast.LENGTH_SHORT).show();
//                Log.e(TAG, "smid: " + deviceId);
            }
        });
    }
}
