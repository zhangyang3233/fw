# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/zhangyang/Library/Android/sdk/tools/proguard/proguard-android.txt
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
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-verbose
##-dontnote

##作为library时候不能使用
-allowaccessmodification

# Optimization is turned off by default. Dex does not like code run
# through the ProGuard optimize and preverify steps (and performs some
# of these optimizations on its own).
-dontoptimize
-dontpreverify
# Note that if you want to enable optimization, you cannot just
# include optimization flags in your own project configuration file;
# instead you will need to point to the
# "proguard-android-optimize.txt" file instead of this one from your
# project.properties file.

-keepattributes *Annotation*
-keepattributes InnerClasses
-keepattributes SourceFile,LineNumberTable
-keepattributes Signature
-keepattributes EnclosingMethod
-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService

# For native methods, see http://proguard.sourceforge.net/manual/examples.html#native
-keepclasseswithmembernames class * {
    native <methods>;
}

# keep setters in Views so that animations can still work.
# see http://proguard.sourceforge.net/manual/examples.html#beans
-keepclassmembers public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    void set*(***);
    *** get*();
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}


# We want to keep methods in Activity that could be used in the XML attribute onClick
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

# For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

# Keep serializable classes and necessary members for serializable classes
# Copied from the ProGuard manual at http://proguard.sourceforge.net.
-keepnames class * implements java.io.Serializable
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# The support library contains references to newer platform versions.
# Don't warn about those in case this app is linking against an older
# platform version.  We know about them, and they are safe.
-keep class android.support.**
-dontwarn android.support.**

-keep public class * extends android.app.Activity
# 保持fragment类名不混淆，方便自动化埋点统计数据还原
-keep public class * extends android.app.Fragment
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
#fix samsung menubuilder not found error
-keep class !android.support.v7.internal.view.menu.*MenuBuilder*, android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }

-keep class com.google.gson.**
-keep interface com.google.gson.**
-keepclasseswithmembers class com.google.gson.** { <methods>; <fields>; }
-keepclasseswithmembers class com.google.gson.** { static <methods>; }
-keepclasseswithmembers class com.google.gson.** { static <fields>; }
-keepclasseswithmembers interface com.google.gson.** { <methods>; }
-keepclasseswithmembers interface com.google.gson.** { static <fields>; }

##################################### rxJava ###########################

-dontwarn sun.misc.**

-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

#########################################################################


############################# keep for Serializable #############################
-keep class * implements java.io.Serializable {*;}
-keep class * implements android.os.Parcelable {*;}
##################################################################################


############################# keep for EventBus #############################
-keepclassmembers,includedescriptorclasses class ** {
    public void onEvent*(**);
}
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
##############################################################################

############################# keep for JavascriptInterface #############################

-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}
##############################################################################


###########################json
-keep class com.fasterxml.jackson.ext.**{ *; }
-keep interface com.fasterxml.jackson.ext.** { *; }
#-dontwarn com.fasterxml.jackson.ext.**{*;}
-keep class org.w3c.dom.**{*;}
-keep interface org.w3c.dom.**{*;}

-keep class com.fasterxml.**{*;}
-keep interface com.fasterxml.**{*;}
#-dontwarn class com.fasterxml.**

-keepnames class org.codehaus.jackson.** { *; }

-dontwarn javax.xml.**
-dontwarn javax.xml.stream.events.**
-dontwarn com.fasterxml.jackson.databind.**
-keepattributes Exceptions

-keep class com.handmark.pulltorefresh.library.** { *; }

-keep class com.zycoder.imageblur.jni.ImageBlur { *; }

-keep class com.squareup.leakcanary.internal { *; }

-keep class * extends java.lang.annotation.Annotation
-keep class com.alipay.euler.andfix.** { *; }

-keep public class * extends android.app.Activity

-dontwarn

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

# Square's wire configuration.

-keep class com.squareup.**{*;}
-keep public class * extends com.squareup.**{*;}
-dontwarn com.squareup.**

# Keep methods with Wire annotations (e.g. @ProtoField)
-keepclassmembers class ** {
    @com.squareup.wire.ProtoField public *;
    @com.squareup.wire.ProtoEnum public *;
}


-dontwarn org.apache.james.mime4j.**
-keep class org.apache.james.mime4j.** {*;}
-dontwarn org.apache.http.entity.mime.**
-keep class org.apache.http.entity.mime.** {*;}

-dontwarn android.support.v4.**
-keep class android.support.v4.** { *; }
-keep public class * extends android.support.v4.**
-keep public class * extends android.app.Fragment

-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**
-keep class com.hongyu.reward.model.**{*;}
-keepclassmembers class com.hongyu.reward.model.** { public * ;protected * ; private *;}

# ----------- 支付宝 -----------
#-libraryjars libs/alipaySdk-20160825.jar
-dontwarn android.net.**
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}
-keep class android.net.SSLCertificateSocketFactory{*;}
# ----------- 支付宝(完) -----------

# ------------ 友盟推送 ------------
-dontwarn com.taobao.**
-dontwarn anet.channel.**
-dontwarn anetwork.channel.**
-dontwarn org.android.**
-dontwarn org.apache.thrift.**
-dontwarn com.xiaomi.**
-dontwarn com.huawei.**

-keepattributes *Annotation*

-keep class com.taobao.** {*;}
-keep class org.android.** {*;}
-keep class anet.channel.** {*;}
-keep class com.umeng.** {*;}
-keep class com.xiaomi.** {*;}
-keep class com.huawei.** {*;}
-keep class org.apache.thrift.** {*;}

-keep public class **.R$*{
   public static final int *;
}
# ------------ 友盟推送(完) ------------
# ------------ 友盟统计 ------------
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
# ------------ 友盟统计(完) ------------


# ------------ 微信 ------------
-keep class com.tencent.mm.sdk.** {
   *;
}
# ------------ 微信(完) ------------

# ------------ litepal ------------
-keep class com.hongyu.reward.model.** {*;}
-dontwarn org.litepal.*
-keep class org.litepal.** { *; }
-keep enum org.litepal.**
-keep interface org.litepal.** { *; }
-keep public class * extends org.litepal.**
-keepattributes *Annotation*
-keepclassmembers enum * { *; }
-keepclassmembers class * extends org.litepal.crud.DataSupport{ *; }
# ------------ litepal(完) ------------

# ------------ 蒲公英 ------------
-dontwarn com.pgyersdk.**
-keep class com.pgyersdk.** { *; }
# ------------ 蒲公英(完) ------------