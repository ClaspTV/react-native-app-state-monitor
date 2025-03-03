import { NativeModules, NativeEventEmitter } from 'react-native';

const { RNAppStateMonitor } = NativeModules;
const AppStateEventEmitter = new NativeEventEmitter(RNAppStateMonitor);

class AppStateMonitor {
  constructor() {
    this._currentState = RNAppStateMonitor.initialAppState || 'unknown';
    this._listeners = new Map();
    this._subscription = null;
    
    // Initialize the subscription
    this._initializeSubscription();
  }
  
  _initializeSubscription() {
    if (this._subscription) {
      this._subscription.remove();
    }
    
    this._subscription = AppStateEventEmitter.addListener(
      'rnAppStateDidChange',
      (event) => {
        if (event && event.app_state && event.app_state !== this._currentState) {
          this._currentState = event.app_state;
          console.log('AppStateMonitor: vzbAppStateDidChange:', this._currentState);
          this._emitChange(this._currentState);
        }
      }
    );
  }
  
  get currentState() {
    return this._currentState;
  }
  
  getCurrentState() {
    return new Promise((resolve) => {
      RNAppStateMonitor.getCurrentState((result) => {
        resolve(result.app_state);
      });
    });
  }
  
  addEventListener(listener) {
    const id = String(Date.now()) + String(Math.random());
    this._listeners.set(id, listener);
    
    if (this._currentState !== 'unknown') {
      try {
        listener(this._currentState);
      } catch (error) {
        console.error('Error in AppState listener callback:', error);
      }
    }
    
    return () => {
      this._listeners.delete(id);
    };
  }
  
  removeAllListeners() {
    this._listeners.clear();
    
    if (this._subscription) {
      this._subscription.remove();
      this._subscription = null;
    }
  }
  
  isForeground() {
    return this._currentState === 'foreground';
  }
  
  isBackground() {
    return this._currentState === 'background';
  }
  
  _emitChange(state) {
    this._listeners.forEach((listener) => {
      try {
        listener(state);
      } catch (error) {
        console.error('Error in AppState listener:', error);
      }
    });
  }
}

export default new AppStateMonitor()
