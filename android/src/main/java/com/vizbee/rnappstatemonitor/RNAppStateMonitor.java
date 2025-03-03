package com.vizbee.rnappstatemonitor;

import android.app.Activity;
import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.Callback;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class RNAppStateMonitor extends ReactContextBaseJavaModule implements LifecycleObserver {
    private static final String TAG = "RNAppStateMonitor";
    private ReactApplicationContext reactContext;
    private String currentAppState = "unknown";
    private Handler mainHandler;

    public RNAppStateMonitor(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        this.mainHandler = new Handler(Looper.getMainLooper());
        
        Log.d(TAG, "Constructor called - Initializing AppStateMonitor");
        
        // Register activity lifecycle callbacks
        // ((Application)(reactContext.getApplicationContext())).registerActivityLifecycleCallbacks(this);
        
        // Safely add lifecycle observer on main thread
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    ProcessLifecycleOwner.get().getLifecycle().addObserver(RNAppStateMonitor.this);
                    Log.d(TAG, "ProcessLifecycleOwner observer added successfully");
                } catch (Exception e) {
                    Log.e(TAG, "Error adding ProcessLifecycleOwner observer", e);
                }
            }
        });
    }

    @NonNull
    @Override
    public String getName() {
        return "RNAppStateMonitor";
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        constants.put("initialAppState", currentAppState);
        Log.d(TAG, "Returning constants - Initial app state: " + currentAppState);
        return constants;
    }

    private void sendAppStateEvent(String appState) {
        // Avoid redundant state changes
        if (currentAppState.equals(appState)) {
            Log.d(TAG, "Skipping redundant state change to: " + appState);
            return;
        }

        Log.d(TAG, "Sending app state event: " + appState + " (Previous state: " + currentAppState + ")");
        currentAppState = appState;
        
        WritableMap params = Arguments.createMap();
        params.putString("app_state", appState);
        
        // Emit event to JS
        if (reactContext != null && reactContext.hasActiveReactInstance()) {
            try {
                reactContext
                    .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit("rnAppStateDidChange", params);
                Log.d(TAG, "App state event emitted successfully");
            } catch (Exception e) {
                Log.e(TAG, "Error emitting app state event", e);
            }
        } else {
            Log.w(TAG, "Cannot emit app state event - no active React instance");
        }
    }

    @ReactMethod
    public void getCurrentState(Callback callback) {
        Log.d(TAG, "getCurrentState called - Current state: " + currentAppState);
        WritableMap result = Arguments.createMap();
        result.putString("app_state", currentAppState);
        callback.invoke(result);
    }

    // ProcessLifecycleOwner events
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onAppForeground() {
        Log.d(TAG, "ProcessLifecycleOwner ON_START");
        sendAppStateEvent("active");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onAppBackground() {
        Log.d(TAG, "ProcessLifecycleOwner ON_STOP");
        sendAppStateEvent("background");
    }

    @Override
    public void onCatalystInstanceDestroy() {
        Log.d(TAG, "Catalyst instance destroyed - Cleaning up");
        super.onCatalystInstanceDestroy();
        try {
            
            // Remove lifecycle observer
            ProcessLifecycleOwner.get().getLifecycle().removeObserver(this);
            
            Log.d(TAG, "Cleanup completed successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error during cleanup", e);
        }
    }
}