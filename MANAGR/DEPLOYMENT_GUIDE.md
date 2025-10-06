# MANAGR Deployment Guide

## Overview

This guide provides step-by-step instructions for deploying the MANAGR application to production, including build configuration, signing, and release management.

## Table of Contents

1. [Build Configuration](#build-configuration)
2. [Code Signing](#code-signing)
3. [Release Build](#release-build)
4. [Play Store Deployment](#play-store-deployment)
5. [Version Management](#version-management)
6. [Monitoring & Analytics](#monitoring--analytics)

## Build Configuration

### Release Build Type
```gradle
android {
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
}
```

### ProGuard Rules
```proguard
# Keep domain models
-keep class com.managr.app.core.domain.model.** { *; }

# Keep Room entities & DAOs
-keep class com.managr.app.core.data.local.** { *; }

# Keep Retrofit interfaces
-keep interface com.managr.app.core.data.remote.api.** { *; }

# Keep Hilt generated classes
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }

# Keep Compose classes
-keep class androidx.compose.** { *; }
```

## Code Signing

### Generate Keystore
```bash
keytool -genkey -v -keystore managr.keystore -alias managr -keyalg RSA -keysize 2048 -validity 10000
```

### Configure Signing
```gradle
android {
    signingConfigs {
        create("release") {
            storeFile = file("../managr.keystore")
            storePassword = System.getenv("KEYSTORE_PASSWORD")
            keyAlias = "managr"
            keyPassword = System.getenv("KEY_PASSWORD")
        }
    }
}
```

## Release Build

### Build Commands
```bash
# Clean and build release
./gradlew clean assembleRelease

# Build and test
./gradlew clean testReleaseUnitTest assembleRelease

# Generate APK
./gradlew assembleRelease

# Generate AAB (recommended for Play Store)
./gradlew bundleRelease
```

### Build Verification
```bash
# Verify APK
aapt dump badging app-release.apk

# Check signing
jarsigner -verify -verbose -certs app-release.apk

# Verify AAB
bundletool validate --bundle=app-release.aab
```

## Play Store Deployment

### App Bundle Configuration
```gradle
android {
    bundle {
        language {
            enableSplit = true
        }
        density {
            enableSplit = true
        }
        abi {
            enableSplit = true
        }
    }
}
```

### Play Console Setup
1. Create app in Play Console
2. Upload app bundle
3. Configure store listing
4. Set up content rating
5. Configure pricing and distribution

### Release Checklist
- [ ] All tests passing
- [ ] Code review completed
- [ ] Performance testing done
- [ ] Accessibility testing completed
- [ ] Security review passed
- [ ] Privacy policy updated
- [ ] Store listing ready
- [ ] Screenshots uploaded
- [ ] App bundle signed
- [ ] Release notes written

## Version Management

### Versioning Strategy
```gradle
android {
    defaultConfig {
        versionCode = 1
        versionName = "1.0.0"
    }
}
```

### Semantic Versioning
- **Major** (1.0.0): Breaking changes
- **Minor** (0.1.0): New features
- **Patch** (0.0.1): Bug fixes

### Version Bumping
```bash
# Update version code
./gradlew updateVersionCode

# Update version name
./gradlew updateVersionName -PversionName=1.0.1
```

## Monitoring & Analytics

### Crash Reporting
```kotlin
// Firebase Crashlytics
implementation 'com.google.firebase:firebase-crashlytics-ktx'

// Initialize in Application
FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
```

### Performance Monitoring
```kotlin
// Firebase Performance
implementation 'com.google.firebase:firebase-perf-ktx'

// Initialize in Application
FirebasePerformance.getInstance().isPerformanceCollectionEnabled = true
```

### Analytics
```kotlin
// Firebase Analytics
implementation 'com.google.firebase:firebase-analytics-ktx'

// Custom events
firebaseAnalytics.logEvent("project_created") {
    param("project_id", projectId)
    param("genre", genre)
}
```

## Security Considerations

### API Key Protection
```kotlin
// Use BuildConfig for API keys
buildConfigField "String", "API_KEY", "\"${project.findProperty("API_KEY")}\""
```

### Network Security
```xml
<!-- network_security_config.xml -->
<network-security-config>
    <domain-config cleartextTrafficPermitted="false">
        <domain includeSubdomains="true">api.releaseflow.com</domain>
    </domain-config>
</network-security-config>
```

### Data Encryption
```kotlin
// Encrypt sensitive data
val encryptedData = encrypt(data, secretKey)
val decryptedData = decrypt(encryptedData, secretKey)
```

## Performance Optimization

### Release Optimizations
- Enable R8/ProGuard
- Enable resource shrinking
- Optimize images
- Use vector drawables
- Enable multidex if needed

### Memory Management
- Monitor memory usage
- Optimize image loading
- Use lazy loading
- Clear caches regularly

## Testing in Production

### Staged Rollout
1. Start with 5% of users
2. Monitor crash reports
3. Check performance metrics
4. Gradually increase to 100%

### A/B Testing
```kotlin
// Firebase A/B Testing
val experiment = firebaseRemoteConfig.getString("experiment_variant")
when (experiment) {
    "variant_a" -> showVariantA()
    "variant_b" -> showVariantB()
    else -> showDefault()
}
```

## Rollback Plan

### Emergency Rollback
1. Identify the issue
2. Revert to previous version
3. Notify users if needed
4. Investigate root cause
5. Fix and redeploy

### Version Rollback
```bash
# Rollback to previous version
./gradlew rollbackVersion

# Deploy previous build
./gradlew deployPreviousVersion
```

## Post-Deployment

### Monitoring
- Check crash reports
- Monitor performance metrics
- Review user feedback
- Track download statistics

### Maintenance
- Regular security updates
- Performance optimizations
- Bug fixes
- Feature updates

This deployment guide ensures a smooth and secure deployment process for the MANAGR application.
