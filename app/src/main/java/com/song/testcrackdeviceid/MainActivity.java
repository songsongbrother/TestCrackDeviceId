package com.song.testcrackdeviceid;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dingxiang.mobile.risk.DXRisk;
import com.ishumei.smantifraud.SmAntiFraud;

/**
 * Created by chensongsong on 2019/9/9.
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Button idBtn;
    private Button dxInitBtn;
    private Button dxIdBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        idBtn = ((Button) findViewById(R.id.show_id));
        dxInitBtn = ((Button) findViewById(R.id.dx_init));
        dxIdBtn = ((Button) findViewById(R.id.dx_id));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permissionArray = {
                    "android.permission.ACCESS_COARSE_LOCATION",
                    "android.permission.ACCESS_FINE_LOCATION",
                    "android.permission.WRITE_EXTERNAL_STORAGE",
                    "android.permission.READ_EXTERNAL_STORAGE",
                    "android.permission.READ_PHONE_STATE",
            };
            this.requestPermissions(permissionArray, 1);
        }

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

            }
        });

        idBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String deviceId = SmAntiFraud.getDeviceId();
                com.qiyi.xhook.XHook.getInstance().refresh(false);

                Toast.makeText(getApplicationContext(), "smid: " + deviceId, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "smid: " + deviceId);
                idBtn.setText(deviceId);
            }
        });

        dxInitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 开通服务后可在实时风险决策的二级菜单“应用管理”中获取
                String appId = "7fb3923f30d03dc3f7020e1d134690d5";
                DXRisk.setup(MainActivity.this, appId);
                com.qiyi.xhook.XHook.getInstance().refresh(false);
            }
        });

        dxIdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 子线程中获取id
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final String token = DXRisk.getToken();
                        Log.e(TAG, "dxid: " + token);
                        com.qiyi.xhook.XHook.getInstance().refresh(false);

                    }
                }).start();
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
