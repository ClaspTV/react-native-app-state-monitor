#import "RNAppStateMonitor.h"
#import <React/RCTUtils.h>
#import <os/log.h>

static const char *kLogTag = "RNAppStateMonitor";

#define RNAppStateLog(format, ...) \
    os_log(OS_LOG_DEFAULT, "%s: " format, kLogTag, ##__VA_ARGS__)

#define RNAppStateLogDebug(format, ...) \
    os_log_debug(OS_LOG_DEFAULT, "%s: " format, kLogTag, ##__VA_ARGS__)

#define RNAppStateLogError(format, ...) \
    os_log_error(OS_LOG_DEFAULT, "%s: " format, kLogTag, ##__VA_ARGS__)

@implementation RNAppStateMonitor {
  NSString *_currentState;
  BOOL _isObserving;
}

RCT_EXPORT_MODULE(RNAppStateMonitor)

- (instancetype)init
{
  if (self = [super init]) {
    // Determine initial state
    UIApplicationState initialState = UIApplication.sharedApplication.applicationState;
    _currentState = initialState == UIApplicationStateActive ? @"active" : @"background";
    
    RNAppStateLogDebug("Initializing RNAppStateMonitor - Initial state: %@", _currentState);
    
    _isObserving = NO;
    
    // Register for application state change notifications
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(handleAppStateDidChange:)
                                                 name:UIApplicationDidBecomeActiveNotification
                                               object:nil];
    
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(handleAppStateDidChange:)
                                                 name:UIApplicationDidEnterBackgroundNotification
                                               object:nil];
    
    RNAppStateLog("Registered for application state change notifications");
  }
  return self;
}

- (void)startObserving 
{
  RNAppStateLogDebug("Starting observation");
  _isObserving = YES;
}

- (void)stopObserving 
{
  RNAppStateLogDebug("Stopping observation");
  _isObserving = NO;
}

- (void)dealloc
{
  RNAppStateLog("Deallocating RNAppStateMonitor - Removing observers");
  [[NSNotificationCenter defaultCenter] removeObserver:self];
}

+ (BOOL)requiresMainQueueSetup
{
  return YES;
}

- (NSDictionary *)constantsToExport
{
  RNAppStateLogDebug("Exporting constants - Initial state: %@", _currentState);
  return @{
    @"initialAppState": _currentState
  };
}

- (NSArray<NSString *> *)supportedEvents
{
  return @[@"rnAppStateDidChange"];
}

- (void)handleAppStateDidChange:(NSNotification *)notification
{
  NSString *newState;
  
  if ([notification.name isEqualToString:UIApplicationDidBecomeActiveNotification]) {
    newState = @"active";
    RNAppStateLog("Application became active");
  } else if ([notification.name isEqualToString:UIApplicationDidEnterBackgroundNotification]) {
    newState = @"background";
    RNAppStateLog("Application entered background");
  } else {
    RNAppStateLogError("Received unexpected notification: %@", notification.name);
    return;
  }
  
  // Check for state change
  if (![newState isEqualToString:_currentState]) {
    RNAppStateLogDebug("State changed from %@ to %@", _currentState, newState);
    
    _currentState = newState;
    
    if (_isObserving) {
      RNAppStateLog("Sending app state change event: %@", newState);
      [self sendEventWithName:@"rnAppStateDidChange" body:@{@"app_state": newState}];
    } else {
      RNAppStateLogDebug("Not sending event - observation is paused");
    }
  } else {
    RNAppStateLogDebug("No state change - still in %@", newState);
  }
}

RCT_EXPORT_METHOD(getCurrentState:(RCTResponseSenderBlock)callback)
{
  RNAppStateLogDebug("getCurrentState called - Returning current state: %@", _currentState);
  callback(@[@{@"app_state": _currentState}]);
}

@end