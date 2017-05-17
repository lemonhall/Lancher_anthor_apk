package com.lsl.lemonhall.startplayer2;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private void doStartApplicationWithPackageName(String packagename) {
        Log.d("lemonhall", "HelloWorld form doStartApplicationWithPackageName!!");

        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        PackageInfo packageinfo = null;
        try {
            packageinfo = getPackageManager().getPackageInfo(packagename, 0);
            Log.d("lemonhall", packageinfo.toString());

        } catch (PackageManager.NameNotFoundException e) {

            Log.d("lemonhall", e.toString());
            e.printStackTrace();
        }
        if (packageinfo == null) {
            Log.d("lemonhall", "packageinfo == null");
            return;
        }

        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        //resolveIntent.addCategory(Intent.CATEGORY_DEFAULT);
        resolveIntent.setPackage(packageinfo.packageName);

        // 通过getPackageManager()的queryIntentActivities方法遍历
        List<ResolveInfo> resolveinfoList = getPackageManager()
                .queryIntentActivities(resolveIntent, 0);

        Log.d("lemonhall", "resolveinfoList.size::"+resolveinfoList.size());



        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null) {
            // packagename = 参数packname
            String packageName = resolveinfo.activityInfo.packageName;
            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
            String className = resolveinfo.activityInfo.name;
            // LAUNCHER Intent
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);

            // 设置ComponentName参数1:packagename参数2:MainActivity路径
            ComponentName cn = new ComponentName(packageName, className);

            intent.setComponent(cn);
            startActivity(intent);
        }else{
            Log.d("lemonhall", "resolveinfo == null");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("lemonhall", "HelloWorld form onCreate!!");
        //我们加入的call高清播放器的代码，做试验用
        doStartApplicationWithPackageName("com.xiaomi.mitv.mediaexplorer");
        //doStartApplicationWithPackageName("com.lsl.lemonhall.startplayer");
    }
}
