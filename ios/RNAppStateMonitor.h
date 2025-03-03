
#if __has_include("RCTBridgeModule.h")
#import "RCTBridgeModule.h"
#else
#import <React/RCTBridgeModule.h>
#endif

#import <React/RCTEventEmitter.h>

@interface RNAppStateMonitor : RCTEventEmitter <RCTBridgeModule>

@property (nonatomic, assign) BOOL isObserving;

@end
  