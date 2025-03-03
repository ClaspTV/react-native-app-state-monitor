# react-native-app-state-monitor

A lightweight React Native module that accurately tracks app foreground/background state using native platform lifecycle observers, ensuring reliable app state detection.

## Features

- Precise app state detection on both Android and iOS
- Uses Android's `ProcessLifecycleOwner` and iOS UIApplication notifications
- Works correctly with multiple activities and complex app lifecycles
- Simple and consistent API
- Supports React Native ≥ 0.60.0

## Installation

```bash
npm install react-native-app-state-monitor
# or
yarn add react-native-app-state-monitor
```

### Automatic linking

For React Native 0.60.0 and above, linking is automatic.

## Usage

```javascript
import AppStateMonitor from 'react-native-app-state-monitor';

// Check current state (may be 'unknown' initially)
console.log('Current state:', AppStateMonitor.currentState);

// Get accurate state asynchronously
AppStateMonitor.getCurrentState().then((state) => {
  console.log('Accurate current state:', state);
});

// Add state change listener
const unsubscribe = AppStateMonitor.addEventListener((newState) => {
  console.log('App state changed to:', newState);
  
  if (newState === 'active') {
    // App is in foreground
    console.log('App moved to foreground');
    // Restart animations, fetch data, etc.
  } else if (newState === 'background') {
    // App is in background
    console.log('App moved to background');
    // Pause operations, save state, etc.
  }
});

// Later, when component unmounts
unsubscribe();

// Alternatively, remove all listeners
// AppStateMonitor.removeAllListeners();
```

### React Hooks Example

```javascript
import React, { useState, useEffect } from 'react';
import { Text, View } from 'react-native';
import AppStateMonitor from 'react-native-app-state-monitor';

const AppStateDemo = () => {
  const [appState, setAppState] = useState(AppStateMonitor.currentState);
  
  useEffect(() => {
    // Get accurate initial state
    AppStateMonitor.getCurrentState().then(setAppState);
    
    // Listen for changes
    const unsubscribe = AppStateMonitor.addEventListener(setAppState);
    
    return () => {
      unsubscribe();
    };
  }, []);
  
  return (
    <View style={{ padding: 20 }}>
      <Text>Current app state: {appState}</Text>
      <Text>Is active: {AppStateMonitor.isActive() ? 'Yes' : 'No'}</Text>
      <Text>Is background: {AppStateMonitor.isBackground() ? 'Yes' : 'No'}</Text>
    </View>
  );
};
```

## How It Works

### Android
The module uses the AndroidX Lifecycle library's `ProcessLifecycleOwner` to monitor the entire application's lifecycle, providing a reliable way to detect app foreground/background states.

### iOS
Uses UIApplication notifications to detect app state changes.

## API

### Supported States
- `'active'`: App is in the foreground
- `'background'`: App is in the background
- `'unknown'`: Initial state or unable to determine

### Properties

- `AppStateMonitor.currentState` - Returns the current state

### Methods

- `AppStateMonitor.getCurrentState()` - Returns a Promise that resolves to the current app state
- `AppStateMonitor.addEventListener(listener)` - Add a listener for state changes and returns a function to remove the listener
- `AppStateMonitor.removeAllListeners()` - Remove all registered listeners
- `AppStateMonitor.isActive()` - Returns true if the app is in the foreground
- `AppStateMonitor.isBackground()` - Returns true if the app is in the background

## Differences from React Native's AppState

- More reliable state detection
- Works correctly with multiple activities
- Provides additional utility methods
- Consistent state values across platforms

## Development and Building

1. Clone the repository
2. Install dependencies: `npm install` or `yarn install`
3. For iOS: `cd ios && pod install`

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

MIT
