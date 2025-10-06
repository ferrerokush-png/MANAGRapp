# Add project specific ProGuard rules here.
# Promotions Feature Module

# Keep ViewModels
-keep class * extends androidx.lifecycle.ViewModel {
    <init>();
}

