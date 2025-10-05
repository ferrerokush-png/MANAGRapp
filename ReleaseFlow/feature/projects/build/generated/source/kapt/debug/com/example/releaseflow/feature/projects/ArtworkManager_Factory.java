package com.example.releaseflow.feature.projects;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation"
})
public final class ArtworkManager_Factory implements Factory<ArtworkManager> {
  private final Provider<Context> contextProvider;

  public ArtworkManager_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public ArtworkManager get() {
    return newInstance(contextProvider.get());
  }

  public static ArtworkManager_Factory create(Provider<Context> contextProvider) {
    return new ArtworkManager_Factory(contextProvider);
  }

  public static ArtworkManager newInstance(Context context) {
    return new ArtworkManager(context);
  }
}
