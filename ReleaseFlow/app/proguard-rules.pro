# MANAGR Security-Hardened ProGuard Rules
# ==========================================

# Preserve line numbers for debugging crash reports
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# ==========================================
# SECURITY: Obfuscation & Code Protection
# ==========================================

# Enable aggressive optimization
-optimizationpasses 5
-dontusemixedcaseclassnames
-verbose

# Remove logging in release builds
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
    public static *** w(...);
    public static *** e(...);
}

# ==========================================
# Security-Critical Classes - DO NOT OBFUSCATE NAMES
# ==========================================

-keep class com.managr.app.core.data.security.** { *; }
-keep class com.managr.app.personal.security.** { *; }

# Keep security exception stack traces
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,EnclosingMethod

# ==========================================
# Android Components
# ==========================================

# Keep Android framework classes
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider

# ==========================================
# Jetpack Compose
# ==========================================

-keep class androidx.compose.** { *; }
-keep @androidx.compose.runtime.Composable class * { *; }

# Keep Compose compiler
-keepclassmembers class androidx.compose.** {
    @androidx.compose.runtime.Composable *;
}

# ==========================================
# Hilt Dependency Injection
# ==========================================

-keep class dagger.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.android.internal.managers.ViewComponentManager$FragmentContextWrapper { *; }

# Keep Hilt generated code
-keep @dagger.hilt.android.** class * { *; }
-keep class **_HiltModules { *; }
-keep class **_Factory { *; }
-keep class **_MembersInjector { *; }

# ==========================================
# Room Database
# ==========================================

-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-keep @androidx.room.Dao class *

# ==========================================
# Kotlin
# ==========================================

-keep class kotlin.** { *; }
-keep class kotlin.Metadata { *; }
-keepclassmembers class **$WhenMappings {
    <fields>;
}

# Keep coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}

# ==========================================
# Retrofit & OkHttp
# ==========================================

-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

-dontwarn okhttp3.**
-dontwarn retrofit2.**
-keep class okhttp3.** { *; }
-keep class retrofit2.** { *; }

# ==========================================
# Gson & JSON
# ==========================================

-keepattributes Signature
-keepattributes *Annotation*
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Keep data model classes (if using Gson)
-keep class com.managr.app.core.domain.model.** { *; }

# ==========================================
# Firebase
# ==========================================

-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }

# ==========================================
# Security: Jetpack Security / EncryptedSharedPreferences
# ==========================================

-keep class androidx.security.crypto.** { *; }
-keep class com.google.crypto.tink.** { *; }

# ==========================================
# Security: Biometric Authentication
# ==========================================

-keep class androidx.biometric.** { *; }

# ==========================================
# Security: SQLCipher
# ==========================================

-keep class net.sqlcipher.** { *; }
-keep class net.sqlcipher.database.** { *; }

# ==========================================
# SafetyNet
# ==========================================

-keep class com.google.android.gms.safetynet.** { *; }

# ==========================================
# WorkManager
# ==========================================

-keep class androidx.work.** { *; }
-keep class * extends androidx.work.Worker
-keep class * extends androidx.work.ListenableWorker

# ==========================================
# Parcelable
# ==========================================

-keepclassmembers class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

# ==========================================
# Native Methods
# ==========================================

-keepclasseswithmembernames class * {
    native <methods>;
}

# ==========================================
# Enums
# ==========================================

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# ==========================================
# Serialization
# ==========================================

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# ==========================================
# Additional Security Hardening
# ==========================================

# Prevent reflection on security classes
-keepnames class com.managr.app.core.data.security.** { *; }
-keepnames class com.managr.app.personal.security.** { *; }

# Encrypt string constants
-adaptclassstrings
-repackageclasses ''
-allowaccessmodification
-optimizeaggressively

# ==========================================
# Warnings to Ignore
# ==========================================

-dontwarn javax.annotation.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**
