# Игнорировать все платформозависимые классы, которые не нужны на десктопе
-dontwarn android.**
-dontwarn com.jogamp.**
-dontwarn javafx.**
-dontwarn org.apache.maven.**
-dontwarn org.apache.maven.plugin.**
-dontwarn org.bytedeco.javacpp.tools.**
-dontwarn org.bytedeco.javacv.**
-dontwarn org.bytedeco.opencv.**
-dontwarn okhttp3.internal.platform.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**
-dontwarn com.sun.jna.**
-dontwarn org.slf4j.impl.**
-dontwarn org.osgi.**

# Если используются динамические вызовы, дополнительные keep-правила не помешают
-keepattributes Signature, InnerClasses, EnclosingMethod, *Annotation*
-keep class com.beust.jcommander.** { *; }
-keep class org.bytedeco.javacpp.** { *; }