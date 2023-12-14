//
//  RCTBridgeModule.h> @interface RCTCalendarModule : NSObject <RCTBridgeModule> RCTCalendarModule.m
//  ReactNativeApp
//
//  Created by TuanNT on 14/12/2023.
//

// RCTCalendarModule.m
#import "React/RCTBridgeModule.h"
#import "React/RCTEventEmitter.h"
//@interface RCT_EXTERN_MODULE(Counter, NSObject)
@interface RCT_EXTERN_REMAP_MODULE(CalendarModule, Counter, RCTEventEmitter)
RCT_EXTERN_METHOD(increment)
RCT_EXTERN_METHOD(getAuth)
@end
