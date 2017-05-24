# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Software\AndroidSDK/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

 -optimizationpasses 5                                                       #指定代码压缩级别
 -dontusemixedcaseclassnames                                                 #混淆时不会产生形形色色的类名
 -dontskipnonpubliclibraryclasses                                            #指定不忽略非公共类库
 -dontpreverify                                                              #不预校验，如果需要预校验，是-dontoptimize
 -ignorewarnings                                                             #屏蔽警告
 -verbose                                                                    #混淆时记录日志
 -optimizations !code/simplification/arithmetic,!field/*,!class/merging/*    #混淆时所采用的算法

# 关闭log
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}

# 混淆后生产映射文件 map 类名->转化后类名的映射
# 存放在app\build\outputs\mapping\release中
-verbose

# 混淆前后的映射
-printmapping mapping.txt

# apk 包内所有 class 的内部结构
-dump class_files.txt

# 未混淆的类和成员
-printseeds seeds.txt

# 列出从 apk 中删除的代码
-printusage unused.txt

# 基本组件
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference

# 如果有引用v4包可以添加下面这行
-keep public class * extends android.support.v4.app.Fragment

# 如果引用了v4或者v7包
-dontwarn android.support.**

# 保护注解
-keepattributes *Annotation*
-keep class * extends java.lang.annotation.Annotation {*;}

# 泛型与反射
-keepattributes Signature
-keepattributes EnclosingMethod

# 不混淆内部类
#-keepattributes InnerClasses

# 保留R下面的资源
-keep class **.R$* {*;}

#支付宝
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}
#百度地图
-dontwarn com.baidu.**
-dontwarn com.baidu.mapapi.**
-keep class com.baidu.** {*;}
-keep class vi.com.gdi.bgl.android.**{*;}
-keep class com.baidu.platform.**{*;}
-keep class com.baidu.location.**{*;}
-keep class com.baidu.vi.**{*;}
#Mob短信
-keep class cn.smssdk.**{*;}
-keep class com.mob.**{*;}
-dontwarn com.mob.**
-dontwarn cn.smssdk.**
#ButterKnife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

### greenDAO 3
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
public static java.lang.String TABLENAME;
}
-keep class **$Properties
# If you do not use SQLCipher:
-dontwarn org.greenrobot.greendao.database.**
# If you do not use RxJava:
-dontwarn rx.**
#实体类不参与混淆
-keep class com.bolong.bochetong.bean.** { *; }
#自定义控件不参与混淆
-keep class com.bolong.bochetong.view.** { *; }

#XBanner 图片轮播混淆配置
-keep class com.stx.xhb.xbanner.**{*;}

-keepattributes Signature #范型
#native方法不混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

#Gson混淆配置
-keep class sun.misc.Unsafe { *; }
-keep class com.idea.fifaalarmclock.entity.***
-keep class com.google.gson.** { *; }


#忽略 libiary 混淆
-keep class io.vov.vitamio.** { *; }

#butterknife不混淆
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}

# OkHttp3
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
-dontwarn okio.**

# Okio
-dontwarn com.squareup.**
-dontwarn okio.**
-keep public class org.codehaus.* { *; }
-keep public class java.nio.* { *; }

-dontwarn com.wuxiaolong.pullloadmorerecyclerview.**
-keep class com.wuxiaolong.pullloadmorerecyclerview.**{*;}

-dontwarn android.support.percent.**
-keep class android.support.percent.**{*;}

-keepnames class com.AnywayAds.Mini$* {
    public <fields>;
    public <methods>;
}

-dontwarn com.nineoldandroids.*
-keep class com.nineoldandroids.** { *;}


