# Add project specific ProGuard rules here.
# Analytics Feature Module

# Keep ViewModels
-keep class * extends androidx.lifecycle.ViewModel {
    <init>();
}

