# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/anlab/Library/Android/sdk/tools/proguard/proguard-android.txt
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
-dontwarn android.support.**

-dontshrink
-dontoptimize
-dontpreverify
-verbose


-dontwarn com.squareup.okhttp.**

-keepattributes *Annotation*
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.app.Fragment
-keep public class vn.com.vshome.foscamsdk.** { *; }

-keep class vn.com.vshome.database.** { *; }

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    *** *Callback(...);
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

 -keepclassmembers public class * extends android.view.View {
  void set*(***);
  *** get*();
 }

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

-assumenosideeffects class android.util.Log {
    public static *** e(...);
    public static *** w(...);
    public static *** wtf(...);
    public static *** d(...);
    public static *** v(...);
}