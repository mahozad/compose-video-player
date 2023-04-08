-keep class io.ktor.** { *; }
-keepclassmembers class io.ktor.** { volatile <fields>; }
-keep class io.ktor.client.engine.cio.** { *; }
-keep class kotlinx.coroutines.** { *; }
-keep class uk.co.caprica.vlcj.** { *; }
-keep class com.sun.jna.** { *; }

-dontwarn kotlinx.atomicfu.**
# See https://github.com/bytedeco/javacv/wiki/Configuring-Proguard-for-JavaCV
-dontwarn org.bytedeco.**
-dontwarn org.opencv.**

#-dontpreverify
#-dontoptimize
#-dontshrink
# Obfuscation breaks coroutines/ktor for some reason
-dontobfuscate
