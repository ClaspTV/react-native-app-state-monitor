# react-native-app-state-monitor

A TypeScript-based React Native module that accurately tracks app foreground/background state using Android's ProcessLifecycleOwner, ensuring reliable app state detection even with multiple activities.

## Features

- Uses Android's `ProcessLifecycleOwner` for reliable app state detection
- Works correctly with multiple activities
- Written in TypeScript with full type definitions
- Simple and consistent API for both platforms
- Supports React Native ≥ 0.60.0

## Installation

```bash
npm install react-native-app-state-monitor --save
# or
yarn add react-native-app-state-monitor
```

### Automatic linking

For React Native 0.60.0 and above, linking is automatic. The library uses autolinking to connect itself to your app.

## Usage

```typescript
import AppState, { AppStateType } from 'react-native-app-state-monitor';

// Check current state (may be 'unknown' initially)
console.log('Current state:', AppState.currentState);

// Get accurate state asynchronously
AppState.getCurrentState().then((state: AppStateType) => {
  console.log('Accurate current state:', state);
});

// Add state change listener
const unsubscribe = AppState.addEventListener((newState: AppStateType) => {
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
// AppState.removeAllListeners();
```

### React Hooks Example

```typescript
import React, { useState, useEffect } from 'react';
import { Text, View } from 'react-native';
import AppState, { AppStateType } from 'react-native-app-state-monitor';

const AppStateDemo: React.FC = () => {
  const [appState, setAppState] = useState<AppStateType>(AppState.currentState);
  
  useEffect(() => {
    // Get accurate initial state
    AppState.getCurrentState().then(setAppState);
    
    // Listen for changes
    const unsubscribe = AppState.addEventListener(setAppState);
    
    return () => {
      unsubscribe();
    };
  }, []);
  
  return (
    <View style={{ padding: 20 }}>
      <Text>Current app state: {appState}</Text>
      <Text>Is active: {AppState.isActive() ? 'Yes' : 'No'}</Text>
      <Text>Is background: {AppState.isBackground() ? 'Yes' : 'No'}</Text>
    </View>
  );
};
```

## How It Works

### Android
The module uses the AndroidX Lifecycle library's `ProcessLifecycleOwner` to monitor the entire application's lifecycle, not just individual activities. This provides a more reliable way to detect app foreground/background states, especially with multiple activities.

### iOS
The module uses UIApplication notifications to detect app state changes.

## API

### Types

```typescript
type AppStateType = 'active' | 'background' | 'unknown';
type AppStateListener = (state: AppStateType) => void;
```

### Properties

- `AppState.currentState: AppStateType` - Returns the current state

### Methods

- `AppState.getCurrentState(): Promise<AppStateType>` - Returns a Promise that resolves to the current app state
- `AppState.addEventListener(listener: AppStateListener): () => void` - Add a listener for state changes and returns a function to remove the listener
- `AppState.removeAllListeners(): void` - Remove all registered listeners
- `AppState.isActive(): boolean` - Returns true if the app is in the foreground
- `AppState.isBackground(): boolean` - Returns true if the app is in the background

## Differences from React Native's AppState

- Uses `ProcessLifecycleOwner` on Android for more reliable detection
- Works correctly with multiple activities
- More consistent state values between platforms
- Full TypeScript support
- Provides additional utility methods like `isActive()` and `isBackground()`

## Development and Building

1. Clone the repository
2. Install dependencies: `yarn install`
3. Build the TypeScript files: `yarn build`

## License

MIT