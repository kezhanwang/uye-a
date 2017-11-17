# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\setup\adt-bundle-windows-x86_64-20140624\adt-bundle-windows-x86_64-20140624\sdk/tools/proguard/proguard-android.txt
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
-dontpreverify
	-optimizationpasses 7
 	-dontusemixedcaseclassnames
	-dontskipnonpubliclibraryclasses
	-dontpreverify
	-verbose
	-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

	-keep public class * extends android.app.Activity
	-keep public class * extends android.app.Application
	-keep public class * extends android.app.Service
	-keep public class * extends android.content.BroadcastReceiver
	-keep public class * extends android.content.ContentProvider
	-keep public class * extends android.app.backup.BackupAgentHelper
	-keep public class * extends android.preference.Preference
	-keep public class com.android.vending.licensing.ILicensingService

	-keep class * implements java.io.Serializable
	-keep class * implements android.os.Parcelable {public static final android.os.Parcelable$Creator *;}
	-keepclassmembers enum * {public static **[] values(); public static ** valueOf(java.lang.String);}

	#所有的属性都不混淆
	-keep public class * implements java.io.Serializable {
    	public *;
	}


		-keepclassmembers class * implements java.io.Serializable {
    	    static final long serialVersionUID;
    	    private static final java.io.ObjectStreamField[] serialPersistentFields;
    	    private void writeObject(java.io.ObjectOutputStream);
    	    private void readObject(java.io.ObjectInputStream);
    	    java.lang.Object writeReplace();
    	    java.lang.Object readResolve();
    	}

    	-keepclasseswithmembernames class * {
    	    native <methods>;
    	}

    	-keep public class * extends android.view.View {
    	    public <init>(android.content.Context);
    	    public <init>(android.content.Context, android.util.AttributeSet);
    	    public <init>(android.content.Context, android.util.AttributeSet, int);
    	    public void set*(...);
    	    public void get*(...);
    	}

    	-keepclasseswithmembernames class * {
    	    public <init>(android.content.Context, android.util.AttributeSet);
    	}

    	-keepclasseswithmembernames class * {
    	    public <init>(android.content.Context, android.util.AttributeSet, int);
    	}

    	-keep public class * implements com.common.listener.NoConfusion
    	-keep public class * implements com.common.listener.NoConfusion {*;}


    #x5
    	-dontwarn com.tencent.smtt.**
    	-keep public class com.tencent.smtt.**{*;}
    	# --------------------------------------------------------------------------
        # Addidional for x5.sdk classes for apps
        -keep class com.tencent.smtt.export.external.**{
            *;
        }

        -keep class com.tencent.tbs.video.interfaces.IUserStateChangedListener {
        	*;
        }

        -keep class com.tencent.smtt.sdk.CacheManager {
        	public *;
        }

        -keep class com.tencent.smtt.sdk.CookieManager {
        	public *;
        }

        -keep class com.tencent.smtt.sdk.WebHistoryItem {
        	public *;
        }

        -keep class com.tencent.smtt.sdk.WebViewDatabase {
        	public *;
        }

        -keep class com.tencent.smtt.sdk.WebBackForwardList {
        	public *;
        }

        -keep public class com.tencent.smtt.sdk.WebView {
        	public <fields>;
        	public <methods>;
        }

        -keep public class com.tencent.smtt.sdk.WebView$HitTestResult {
        	public static final <fields>;
        	public java.lang.String getExtra();
        	public int getType();
        }

        -keep public class com.tencent.smtt.sdk.WebView$PictureListener {
        	public <fields>;
        	public <methods>;
        }


        -keepattributes InnerClasses

        -keep public enum com.tencent.smtt.sdk.WebSettings$** {
            *;
        }

        -keep public class com.tencent.smtt.sdk.WebSettings {
            public *;
        }


        -keepattributes Signature
        -keep public class com.tencent.smtt.sdk.ValueCallback {
        	public <fields>;
        	public <methods>;
        }

        -keep public class com.tencent.smtt.sdk.WebViewClient {
        	public <fields>;
        	public <methods>;
        }

        -keep public class com.tencent.smtt.sdk.DownloadListener {
        	public <fields>;
        	public <methods>;
        }

        -keep public class com.tencent.smtt.sdk.WebChromeClient {
        	public <fields>;
        	public <methods>;
        }

        -keep class com.tencent.smtt.sdk.SystemWebChromeClient{
        	public *;
        }
        # 1. extension interfaces should be apparent
        -keep public class com.tencent.smtt.export.external.extension.interfaces.* {
        	public protected *;
        }

        # 2. interfaces should be apparent
        -keep public class com.tencent.smtt.export.external.interfaces.* {
        	public protected *;
        }

        -keep public class com.tencent.smtt.sdk.WebViewCallbackClient {
        	public protected *;
        }

        -keep public class com.tencent.smtt.sdk.WebStorage$QuotaUpdater {
        	public <fields>;
        	public <methods>;
        }

        -keep public class com.tencent.smtt.sdk.DownloadListener {
        	public <fields>;
        	public <methods>;
        }

        -keep public class com.tencent.smtt.sdk.QbSdk {
        	public <fields>;
        	public <methods>;
        }

        -keep public class com.tencent.smtt.sdk.CookieSyncManager {
        	public <fields>;
        	public <methods>;
        }

        -keep public class com.tencent.smtt.sdk.Tbs* {
        	public <fields>;
        	public <methods>;
        }

        -keep public class com.tencent.smtt.utils.LogFileUtils {
        	public <fields>;
        	public <methods>;
        }

        -keep public class com.tencent.smtt.utils.TbsLog {
        	public <fields>;
        	public <methods>;
        }

        -keep public class com.tencent.smtt.utils.TbsLogClient {
        	public <fields>;
        	public <methods>;
        }

        -keep public class com.tencent.smtt.sdk.CookieSyncManager {
        	public <fields>;
        	public <methods>;
        }


        -keep public class com.tencent.smtt.export.external.extension.proxy.ProxyWebViewClientExtension {
        	public <fields>;
        	public <methods>;
        }

        -keep class MTT.ThirdAppInfoNew {
        	*;
        }


#glide相关 不混淆
-keep class com.bumptech.glide.**{*;}


#人脸识别混淆相关
-keep class com.face.** {*;}
-keep class cn.com.bsfit.** {*;}
-keep class com.android.snetjob.** {*;}
-keep class java.awt.** { *; }
-dontwarn com.sun.jna.**
-keep class com.sun.jna.** { *; }
-keep class com.udcredit.** { *; }
-keep class com.authreal.** { *; }
-keep class com.facevisa.** { *; }
-keep class com.hotvision.** { *; }




#百度地图定位
-keep class com.baidu.** {*;}
-keep class vi.com.** {*;}
-dontwarn com.baidu.**

#bugly相关配置
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

#极光推送
-dontoptimize
-dontpreverify
-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }

#http lib相关
-dontwarn org.apache.http.**
-keep class org.apache.http.** { *; }


 #gson相关
-keep class com.google.**{*;}
-keep class sun.misc.Unsafe { *; }
-keepattributes *Annotation*
-keepattributes Signature
-keep class com.google.gson.stream.** { *; }
