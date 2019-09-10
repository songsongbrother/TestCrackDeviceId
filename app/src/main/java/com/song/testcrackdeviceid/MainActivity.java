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
                com.qiyi.xhook.XHook.getInstance().refresh(false);

//                String deviceId = SmAntiFraud.getDeviceId();
//
//                Toast.makeText(getApplicationContext(), "smid: " + deviceId, Toast.LENGTH_SHORT).show();
//                Log.e(TAG, "smid: " + deviceId);
            }
        });

        //load xhook
        com.qiyi.xhook.XHook.getInstance().init(this.getApplicationContext());
        if (!com.qiyi.xhook.XHook.getInstance().isInited()) {
            return;
        }
        //com.qiyi.xhook.XHook.getInstance().enableDebug(true); //default is false
        //com.qiyi.xhook.XHook.getInstance().enableSigSegvProtection(false); //default is true

        //load and run your biz lib (for register hook points)
        com.qiyi.biz.Biz.getInstance().init();
        com.qiyi.biz.Biz.getInstance().start();

        //xhook do refresh
        com.qiyi.xhook.XHook.getInstance().refresh(false);

        //load and run the target lib
//        com.qiyi.test.Test.getInstance().init();
//        com.qiyi.test.Test.getInstance().start();
//        try {
//            Thread.sleep(200);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        //xhook do refresh again
//        com.qiyi.xhook.XHook.getInstance().refresh(false);

        //xhook do refresh again for some reason,
        //maybe called after some System.loadLibrary() and System.load()

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    com.qiyi.xhook.XHook.getInstance().refresh(true);

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
