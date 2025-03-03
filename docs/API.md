# API Documentation

## Overview

`react-native-app-state-monitor` provides a robust, cross-platform solution for tracking app state changes in React Native applications.

## Import

```javascript
import AppStateMonitor from 'react-native-app-state-monitor';
```

## States

The module supports three app states:

- `'active'`: The app is in the foreground and receiving user interactions
- `'background'`: The app is not visible to the user
- `'unknown'`: Initial state or unable to determine current state

## Properties

### `currentState`

**Type**: `string`

Returns the current app state.

```javascript
const currentState = AppStateMonitor.currentState;
console.log(currentState); // 'active', 'background', or 'unknown'
```

## Methods

### `getCurrentState()`

**Returns**: `Promise<string>`

Asynchronously retrieves the current app state.

```javascript
AppStateMonitor.getCurrentState().then((state) => {
  console.log('Current state:', state);
});
```

### `addEventListener(listener)`

**Parameters**:
- `listener`: `(state: string) => void`

**Returns**: `() => void` (unsubscribe function)

Adds a listener for app state changes.

```javascript
const unsubscribe = AppStateMonitor.addEventListener((newState) => {
  console.log('App state changed to:', newState);
});

// Remove the listener when no longer needed
unsubscribe();
```

### `removeAllListeners()`

Removes all registered state change listeners.

```javascript
AppStateMonitor.removeAllListeners();
```

### `isActive()`

**Returns**: `boolean`

Checks if the app is in the active state.

```javascript
const isAppActive = AppStateMonitor.isActive();
```

### `isBackground()`

**Returns**: `boolean`

Checks if the app is in the background state.

```javascript
const isAppBackground = AppStateMonitor.isBackground();
```

## Complete Example

```javascript
import React, { useEffect } from 'react';
import { View, Text } from 'react-native';
import AppStateMonitor from 'react-native-app-state-monitor';

const AppStateDemo = () => {
  useEffect(() => {
    // Log initial state
    console.log('Initial state:', AppStateMonitor.currentState);

    // Add state change listener
    const unsubscribe = AppStateMonitor.addEventListener((newState) => {
      if (newState === 'active') {
        // Perform actions when app becomes active
        console.log('App is now active');
      } else if (newState === 'background') {
        // Perform actions when app goes to background
        console.log('App is now in background');
      }
    });

    // Cleanup listener on component unmount
    return () => {
      unsubscribe();
    };
  }, []);

  return (
    <View>
      <Text>Current App State: {AppStateMonitor.currentState}</Text>
      <Text>Is Active: {AppStateMonitor.isActive() ? 'Yes' : 'No'}</Text>
    </View>
  );
};

export default AppStateDemo;
```

## Platform Specifics

### Android

- Uses `ProcessLifecycleOwner` for state detection
- More reliable with multiple activities

### iOS

- Uses UIApplication notifications
- Provides consistent state tracking

## Troubleshooting

- Ensure you're using React Native ≥ 0.60.0
- Check that the module is correctly linked
- Verify platform-specific configurations