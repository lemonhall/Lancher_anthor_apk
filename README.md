1、启动用代码
===========

```
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
```

2、调用方式
==========

//我们加入的call高清播放器的代码，做试验用
doStartApplicationWithPackageName("com.xiaomi.mitv.mediaexplorer");

3、问题
======
对方的包内部的Activity有哪些，我是不知道的，首先我是通过幸运破解器，找到了系统级应用的包名；
然后想办法备份下来apk
之后用brew install apktool
安装了反编译工具
用apktool d player.apk
的方式得到了具体的AndroidManifest.xml
发现对方的category设置的非常诡异，导致
打印的时候你会发现：D/lemonhall: resolveinfoList.size::0
也就是说，找不到任何的Activity
改了半天也没结果，后来，试了试

```
 // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent  Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);     
//resolveIntent.addCategory(Intent.CATEGORY_DEFAULT);  resolveIntent.setPackage(packageinfo.packageName);
```

将//resolveIntent.addCategory(Intent.CATEGORY_DEFAULT); 注释掉了
然后就得到了两个ctivity；
OK，然后启动第一个，就已经如果所愿了；
这样就解决了，一些奇怪的系统内APP，无法被桌面调用的问题；
相当于加了一个快捷方式；


