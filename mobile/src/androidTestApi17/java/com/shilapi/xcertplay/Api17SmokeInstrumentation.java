package com.shilapi.xcertplay;

import android.app.Instrumentation;
import android.os.Bundle;
import androidx.multidex.MultiDex;
import dalvik.system.DexClassLoader;

/** A Java-only bootstrap avoids verifying Kotlin references before multidex installation. */
public final class Api17SmokeInstrumentation extends Instrumentation {
    @Override public void onCreate(Bundle arguments) {
        MultiDex.installInstrumentation(getContext(), getTargetContext());
        super.onCreate(arguments);
        start();
    }

    @Override public void onStart() {
        try {
            ClassLoader loader = new DexClassLoader(getContext().getApplicationInfo().sourceDir,
                getTargetContext().getDir("api17-smoke", 0).getAbsolutePath(), null,
                getTargetContext().getClassLoader());
            Class<?> checks = Class.forName("com.shilapi.xcertplay.Api17SmokeChecks", true, loader);
            checks.getMethod("run", Instrumentation.class).invoke(checks.getConstructor().newInstance(), this);
        } catch (Throwable failure) {
            Bundle result = new Bundle();
            result.putString("bootstrap_failure", android.util.Log.getStackTraceString(failure));
            finish(0, result);
        }
    }
}
