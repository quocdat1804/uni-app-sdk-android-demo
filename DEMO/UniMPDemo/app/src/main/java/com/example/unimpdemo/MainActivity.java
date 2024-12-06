package com.example.unimpdemo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowInsetsController;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.example.unimpdemo.util.DownloadUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import io.dcloud.feature.sdk.DCUniMPSDK;
import io.dcloud.feature.sdk.Interface.IDCUniMPOnCapsuleCloseButtontCallBack;
import io.dcloud.feature.sdk.Interface.IMenuButtonClickCallBack;
import io.dcloud.feature.sdk.Interface.IOnUniMPEventCallBack;
import io.dcloud.feature.sdk.Interface.IUniMP;
import io.dcloud.feature.sdk.Interface.IUniMPOnCloseCallBack;
import io.dcloud.feature.unimp.DCUniMPJSCallback;
import io.dcloud.feature.unimp.config.IUniMPReleaseCallBack;
import io.dcloud.feature.unimp.config.UniMPOpenConfiguration;
import io.dcloud.feature.unimp.config.UniMPReleaseConfiguration;

public class MainActivity extends Activity {
    Context mContext;
    Handler mHandler;
    /** unimp applet instance cache**/
    HashMap<String, IUniMP> mUniMPCaches = new HashMap<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mHandler = new Handler();
        setContentView(R.layout.main);
        Button button1 = findViewById(R.id.button1);

//        if(Build.VERSION.SDK_INT >= 35){
//
//            WindowInsetsController windowInsetsController = getWindow().getDecorView().getWindowInsetsController();
//            WindowInsetsControllerCompat controller = windowInsetsController != null
//                    ? WindowInsetsControllerCompat.toWindowInsetsControllerCompat(
//                    windowInsetsController) : null;
//
////            WindowInsetsControllerCompat controller = ViewCompat.getWindowInsetsController(getWindow().getDecorView());
//            if(controller != null){
//                controller.setAppearanceLightStatusBars(true);
//                controller.setAppearanceLightNavigationBars(true);
//            }
//        }


        //Used to test whether the click event of sdkDemo capsule× is effective; lxl adds
        DCUniMPSDK.getInstance().setCapsuleCloseButtonClickCallBack(new IDCUniMPOnCapsuleCloseButtontCallBack() {
            @Override
            public void closeButtonClicked(String appid) {
                Toast.makeText(mContext, "Click on the ×", Toast.LENGTH_SHORT).show();
                if(mUniMPCaches.containsKey(appid)) {
                    IUniMP mIUniMP = mUniMPCaches.get(appid);
                    if (mIUniMP != null && mIUniMP.isRuning()){
                        mIUniMP.closeUniMP();
                        mUniMPCaches.remove(appid) ;
                    }
                }
            }
        });


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    UniMPOpenConfiguration uniMPOpenConfiguration = new UniMPOpenConfiguration();
                    uniMPOpenConfiguration.splashClass = MySplashView.class;
                    uniMPOpenConfiguration.extraData.put("darkmode", "light");
                    IUniMP uniMP = DCUniMPSDK.getInstance().openUniMP(mContext,"__UNI__F743940", uniMPOpenConfiguration);
                    mUniMPCaches.put(uniMP.getAppid(), uniMP);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        Button button2 = findViewById(R.id.button2);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    IUniMP uniMP = DCUniMPSDK.getInstance().openUniMP(mContext,"__UNI__F743940");
                    mUniMPCaches.put(uniMP.getAppid(), uniMP);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Button button3 = findViewById(R.id.button3);

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    UniMPOpenConfiguration uniMPOpenConfiguration = new UniMPOpenConfiguration();
                    uniMPOpenConfiguration.path= "pages/API/image/image";
                    IUniMP uniMP = DCUniMPSDK.getInstance().openUniMP(mContext,"__UNI__F743940", uniMPOpenConfiguration);
                    mUniMPCaches.put(uniMP.getAppid(), uniMP);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Button button4 = findViewById(R.id.button4);

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    UniMPOpenConfiguration uniMPOpenConfiguration = new UniMPOpenConfiguration();
                    uniMPOpenConfiguration.path = "/pages/component/view/view";

                    final IUniMP uniMP = DCUniMPSDK.getInstance().openUniMP(mContext,"__UNI__F743940", uniMPOpenConfiguration);
                    mUniMPCaches.put(uniMP.getAppid(), uniMP);
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("unimp", "After a delay of 5 seconds, the current applet will be closed.");
                            uniMP.closeUniMP();
                        }
                    }, 5000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Button button5 = findViewById(R.id.button5);

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject info = DCUniMPSDK.getInstance().getAppVersionInfo("__UNI__F743940");
                if(info != null) {
                    Log.e("unimp", "info==="+info.toString());
                }
            }
        });

        Button button6 = findViewById(R.id.button6);
            button6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        IUniMP uniMP = DCUniMPSDK.getInstance().openUniMP(mContext,"__UNI__B61D13B");
                        mUniMPCaches.put(uniMP.getAppid(), uniMP);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        Button button7 = findViewById(R.id.button7);
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    Intent intent = new Intent(MainActivity.this,DemoForeService.class);
                    startForegroundService(intent);
                }


                try {
                    UniMPOpenConfiguration uniMPOpenConfiguration = new UniMPOpenConfiguration();
                    uniMPOpenConfiguration.redirectPath = "pages/sample/send-event";

                    IUniMP uniMP = DCUniMPSDK.getInstance().openUniMP(mContext,"__UNI__B61D13B", uniMPOpenConfiguration);
                    mUniMPCaches.put(uniMP.getAppid(), uniMP);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Button button8 = findViewById(R.id.button8);
        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    UniMPOpenConfiguration uniMPOpenConfiguration = new UniMPOpenConfiguration();
                    uniMPOpenConfiguration.redirectPath = "pages/sample/ext-module";
                    uniMPOpenConfiguration.extraData.put("darkmode", "dark");
                    IUniMP uniMP = DCUniMPSDK.getInstance().openUniMP(mContext,"__UNI__B61D13B", uniMPOpenConfiguration);
                    mUniMPCaches.put(uniMP.getAppid(), uniMP);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        DCUniMPSDK.getInstance().setDefMenuButtonClickCallBack(new IMenuButtonClickCallBack() {
            @Override
            public void onClick(String appid, String id) {
                switch (id) {
                    case "gy":{
                        Log.e("unimp", "Click on About" + appid);
                        //宿主主动触发事件
                        JSONObject data = new JSONObject();
                        try {
                            IUniMP uniMP = mUniMPCaches.get(appid);
                            if(uniMP != null) {
                                data.put("sj", "Click on About");
                                uniMP.sendUniMPEvent("gy", data);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    case "hqdqym" :{
                        IUniMP uniMP = mUniMPCaches.get(appid);
                        if(uniMP != null) {
                            Log.e("unimp", "Current page url=" + uniMP.getCurrentPageUrl());
                        } else {
                            Log.e("unimp", "No relevant applet instances found");
                        }
                        break;
                    }
                    case "gotoTestPage" :
                        Intent intent = new Intent();
                        intent.setClassName(mContext, "com.example.unimpdemo.TestPageActivity");
                        DCUniMPSDK.getInstance().startActivityForUniMPTask(appid, intent);
                        break;
                }
            }
        });


        Button btn_encrypt_wgt_install = findViewById(R.id.btn_encrypt_wgt_install);
        btn_encrypt_wgt_install.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]
                                {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1002);
                // Remote download and installation
                updateWgt();
            }
        });

        DCUniMPSDK.getInstance().setOnUniMPEventCallBack(new IOnUniMPEventCallBack() {
            @Override
            public void onUniMPEventReceive(String appid, String event, Object data, DCUniMPJSCallback callback) {
                Log.i("cs", "onUniMPEventReceive    event="+event);
                //回传数据给小程序
                callback.invoke( "Receive message");
            }
        });

        checkPermission();
    }

    /**
     * Simulate update wgt
     */
    private void updateWgt() {
        //
        final String wgtUrl = "http://192.168.1.54:3011/download/__UNI__G0B2E111.wgt";
        final String wgtName = "__UNI__G0B2E111.wgt";

        String downFilePath = getExternalCacheDir().getPath();

        Handler uiHandler = new Handler();


        DownloadUtil.get().download(MainActivity.this, wgtUrl, downFilePath, wgtName, new DownloadUtil.OnDownloadListener() {

            @Override
            public void onDownloadSuccess(File file) {


                UniMPReleaseConfiguration uniMPReleaseConfiguration = new UniMPReleaseConfiguration();
                uniMPReleaseConfiguration.wgtPath = file.getPath();
//                uniMPReleaseConfiguration.password = "789456123";

                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        DCUniMPSDK.getInstance().releaseWgtToRunPath("__UNI__G0B2E111", uniMPReleaseConfiguration, new IUniMPReleaseCallBack() {
                            @Override
                            public void onCallBack(int code, Object pArgs) {
                                if(code ==1) {
                                    //Release wgt completed
                                    try {
                                        UniMPOpenConfiguration uniMPOpenConfiguration = new UniMPOpenConfiguration();
                                        uniMPOpenConfiguration.extraData.put("darkmode", "auto");
                                        DCUniMPSDK.getInstance().openUniMP(MainActivity.this, "__UNI__G0B2E111",uniMPOpenConfiguration);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else{
                                    //Failed to release wgt
                                }
                            }
                        });
                    }
                });


            }

            @Override
            public void onDownloading(int progress) {

            }

            @Override
            public void onDownloadFailed() {
                Log.e("unimp", "downFilePath  ===  onDownloadFailed");
            }
        });
    }
    
    /**
     * Check and apply for permissions
     */
    public void checkPermission() {
        int targetSdkVersion = 0;
        String[] PermissionString={Manifest.permission.WRITE_EXTERNAL_STORAGE};
        try {
            final PackageInfo info = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            targetSdkVersion = info.applicationInfo.targetSdkVersion;//Get the Target version of the application
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Build.VERSION.SDK_INT is to get the current phone version Build.VERSION_CODES.M is 6.0 system
//If the system>=6.0
            if (targetSdkVersion >= Build.VERSION_CODES.M) {
                //Step 1: Check if you have the appropriate permissions
                boolean isAllGranted = checkPermissionAllGranted(PermissionString);
                if (isAllGranted) {
                    Log.e("err","All permissions have been authorized！");
                    return;
                }
                // Request multiple permissions at a time. If other permissions are already granted, they will be automatically ignored.
                ActivityCompat.requestPermissions(this, PermissionString, 1);
            }
        }
    }

    /**
     * 检查是否拥有指定的所有权限
     */
    private boolean checkPermissionAllGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                // As long as one permission is not granted, false will be returned directly
//Log.e("err","permission"+permission+"not authorized");
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUniMPCaches.clear();
    }
}