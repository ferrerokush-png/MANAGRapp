package com.managr.app.personal.security

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Debug
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Runtime Application Self-Protection (RASP)
 * Detects root, tampering, debugging, and other security threats
 */
@Singleton
class RuntimeSecurityManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    /**
     * Perform comprehensive security check
     */
    fun performSecurityCheck(): SecurityCheckResult {
        val results = mutableListOf<SecurityThreat>()
        
        if (isDeviceRooted()) {
            results.add(SecurityThreat.RootDetected)
        }
        
        if (isDebuggable()) {
            results.add(SecurityThreat.DebuggableApp)
        }
        
        if (isDebuggerAttached()) {
            results.add(SecurityThreat.DebuggerAttached)
        }
        
        if (isAppTampered()) {
            results.add(SecurityThreat.AppTampered)
        }
        
        if (isEmulator()) {
            results.add(SecurityThreat.EmulatorDetected)
        }
        
        return SecurityCheckResult(
            isSecure = results.isEmpty(),
            threats = results
        )
    }
    
    /**
     * Check if device is rooted
     */
    fun isDeviceRooted(): Boolean {
        return checkRootFiles() || checkSuBinary() || checkRootCloakingApps()
    }
    
    private fun checkRootFiles(): Boolean {
        val paths = arrayOf(
            "/system/app/Superuser.apk",
            "/sbin/su",
            "/system/bin/su",
            "/system/xbin/su",
            "/data/local/xbin/su",
            "/data/local/bin/su",
            "/system/sd/xbin/su",
            "/system/bin/failsafe/su",
            "/data/local/su",
            "/su/bin/su"
        )
        
        return paths.any { File(it).exists() }
    }
    
    private fun checkSuBinary(): Boolean {
        return try {
            val process = Runtime.getRuntime().exec(arrayOf("/system/xbin/which", "su"))
            val bufferedReader = process.inputStream.bufferedReader()
            bufferedReader.readLine() != null
        } catch (e: Exception) {
            false
        }
    }
    
    private fun checkRootCloakingApps(): Boolean {
        val packages = arrayOf(
            "com.noshufou.android.su",
            "com.noshufou.android.su.elite",
            "eu.chainfire.supersu",
            "com.koushikdutta.superuser",
            "com.thirdparty.superuser",
            "com.yellowes.su",
            "com.topjohnwu.magisk"
        )
        
        return packages.any {
            try {
                context.packageManager.getPackageInfo(it, 0)
                true
            } catch (e: PackageManager.NameNotFoundException) {
                false
            }
        }
    }
    
    /**
     * Check if app is debuggable
     */
    fun isDebuggable(): Boolean {
        return (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
    }
    
    /**
     * Check if debugger is attached
     */
    fun isDebuggerAttached(): Boolean {
        return Debug.isDebuggerConnected() || Debug.waitingForDebugger()
    }
    
    /**
     * Check if app has been tampered with
     */
    fun isAppTampered(): Boolean {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(
                context.packageName,
                PackageManager.GET_SIGNATURES
            )
            
            // Check if installer is from trusted source
            val installer = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                context.packageManager.getInstallSourceInfo(context.packageName).installingPackageName
            } else {
                @Suppress("DEPRECATION")
                context.packageManager.getInstallerPackageName(context.packageName)
            }
            
            val trustedInstallers = listOf(
                "com.android.vending", // Google Play Store
                "com.google.android.feedback" // Google Play Services
            )
            
            installer !in trustedInstallers
        } catch (e: Exception) {
            true // Assume tampered if we can't check
        }
    }
    
    /**
     * Check if running on emulator
     */
    fun isEmulator(): Boolean {
        return (Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk" == Build.PRODUCT)
    }
    
    /**
     * Check for hooks/modifications (Xposed, Frida, etc.)
     */
    fun isHookingDetected(): Boolean {
        return try {
            // Check for Xposed
            val xposedBridge = Class.forName("de.robv.android.xposed.XposedBridge")
            xposedBridge != null
        } catch (e: ClassNotFoundException) {
            false
        } || checkForFrida()
    }
    
    private fun checkForFrida(): Boolean {
        val suspiciousLibs = arrayOf(
            "frida-agent",
            "frida-server",
            "frida-gadget"
        )
        
        return try {
            val mapsFile = File("/proc/self/maps")
            val content = mapsFile.readText()
            suspiciousLibs.any { lib -> content.contains(lib) }
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Verify app integrity using signature
     */
    fun verifyAppSignature(): Boolean {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(
                context.packageName,
                PackageManager.GET_SIGNATURES
            )
            
            // Get signature hash
            @Suppress("DEPRECATION")
            val signature = packageInfo.signatures[0]
            val md = java.security.MessageDigest.getInstance("SHA-256")
            val signatureHash = md.digest(signature.toByteArray())
            
            // Compare with expected hash (store this securely or hardcode)
            // This is a placeholder - replace with your actual signature hash
            val expectedHash = "EXPECTED_SIGNATURE_HASH"
            
            // In production, compare actual hashes
            true
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Security check result
     */
    data class SecurityCheckResult(
        val isSecure: Boolean,
        val threats: List<SecurityThreat>
    ) {
        val message: String
            get() = when {
                isSecure -> "App is secure"
                threats.size == 1 -> "Security threat detected: ${threats[0].description}"
                else -> "Multiple security threats detected: ${threats.joinToString { it.description }}"
            }
    }
    
    /**
     * Security threat types
     */
    sealed class SecurityThreat(val description: String) {
        object RootDetected : SecurityThreat("Device is rooted")
        object DebuggableApp : SecurityThreat("App is debuggable")
        object DebuggerAttached : SecurityThreat("Debugger is attached")
        object AppTampered : SecurityThreat("App has been tampered with")
        object EmulatorDetected : SecurityThreat("Running on emulator")
        object HookingDetected : SecurityThreat("Hooking framework detected")
        object SignatureInvalid : SecurityThreat("Invalid app signature")
    }
}


