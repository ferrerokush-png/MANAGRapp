# Add project specific ProGuard rules here.
# Calendar Feature Module

# Keep ViewModels
-keep class * extends androidx.lifecycle.ViewModel {
    <init>();
}

