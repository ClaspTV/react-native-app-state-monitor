# React Native App State Monitor

<p align="center">
  <a href="https://vizbee.tv">
    <img src="https://avatars.githubusercontent.com/u/10885409?s=96&v=4" alt="Vizbee Logo" width="300"/>
  </a>
</p>

<p align="center">
  <a href="https://github.com/ClaspTV/react-native-app-state-monitor/actions">
    <img src="https://github.com/ClaspTV/react-native-app-state-monitor/workflows/CI/badge.svg" alt="CI Status"/>
  </a>
  <a href="https://www.npmjs.com/package/react-native-app-state-monitor">
    <img src="https://img.shields.io/npm/v/react-native-app-state-monitor.svg" alt="NPM Version"/>
  </a>
  <a href="https://github.com/ClaspTV/react-native-app-state-monitor/blob/main/LICENSE">
    <img src="https://img.shields.io/github/license/ClaspTV/react-native-app-state-monitor.svg" alt="License"/>
  </a>
  <img src="https://img.shields.io/badge/platforms-iOS%20%7C%20Android-brightgreen" alt="Platforms"/>
</p>

## 🌟 About

A lightweight React Native module that accurately tracks app foreground/background state using native platform lifecycle observers, ensuring reliable app state detection.

## ✨ Features

- 🔍 Precise app state detection on both Android and iOS
- 🏗️ Uses Android's `ProcessLifecycleOwner` and iOS UIApplication notifications
- 🔀 Works correctly with multiple activities and complex app lifecycles
- 🧩 Simple and consistent API
- 📱 Supports React Native ≥ 0.60.0

## 📦 Installation

```bash
npm install react-native-app-state-monitor
# or
yarn add react-native-app-state-monitor
```

## 🚀 Usage

```javascript
import AppStateMonitor from 'react-native-app-state-monitor';

// Check current state
console.log('Current state:', AppStateMonitor.currentState);

// Add state change listener
const unsubscribe = AppStateMonitor.addEventListener((newState) => {
  console.log('App state changed to:', newState);
});
```

## 📖 Full Documentation

[View Complete Documentation](docs/API.md)

## 🤝 Contributing

Contributions are welcome! Please read our [Contributing Guidelines](CONTRIBUTING.md) before getting started.

### 🐛 Reporting Issues

If you encounter any problems, please [open an issue](https://github.com/ClaspTV/react-native-app-state-monitor/issues/new/choose) with a clear description of the problem.

## 💡 Why This Module?

React Native's built-in `AppState` has known limitations, particularly on Android. This module provides:

- 🔒 More reliable state detection
- 🔄 Consistent behavior across platforms
- 🛠️ Advanced tracking for multi-activity scenarios

## 📋 Requirements

- React Native ≥ 0.60.0
- iOS ≥ 10.0
- Android API level ≥ 21

## 🔒 License

[MIT License](LICENSE)

## 🏢 About Vizbee

[Vizbee](https://vizbee.tv) is transforming fragmented experiences across mobile and streaming devices in a home into one seamless app experience to increase viewer acquisition and monetization.

---