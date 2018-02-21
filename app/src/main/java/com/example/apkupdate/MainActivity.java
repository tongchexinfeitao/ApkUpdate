package com.example.apkupdate;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    public static final String APK_URL = "http://111.205.104.180/bwie.1.apk";
    private long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //    public void check(View view) {
//        Observable<VersionBean> versionBeanObservable = RetrofitManager.getInstance().create(CheckVersionApi.class).checkVersion(getVersionCode());
//        versionBeanObservable
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<VersionBean>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(VersionBean versionBean) {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
//    }


    public void check(View view) {
        final DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(APK_URL);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(false);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle("app标题");
        request.setDescription("正在更新");
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "my.apk");
        id = downloadManager.enqueue(request);




        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equalsIgnoreCase(downloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                    Toast.makeText(MainActivity.this, "chenggong", Toast.LENGTH_LONG).show();

                    DownloadManager.Query query = new DownloadManager.Query().setFilterById(id);
                    Cursor cursor = downloadManager.query(query);
                    if (cursor != null && cursor.moveToNext()) {
                        long aLong = cursor.getLong(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS));
                        if (aLong == DownloadManager.STATUS_SUCCESSFUL) {
                            installApk();
                        }
                    }
                }
            }
        }, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    private void installApk() {
        File apkFile =
                new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "my.apk");

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        startActivity(intent);
    }


    //拿到当前版本号
    private int getVersionCode() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
