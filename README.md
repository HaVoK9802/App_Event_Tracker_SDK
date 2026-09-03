
# App Event Tracker Simulator

 - This is the Android SDK that apps can use to track events for Analytics.
 - Find the Sample Consumer App that utilizes this SDK: https://github.com/HaVoK9802/App_Event_Tracker_Consumer_APP


## SDK Dependency

- Dependency for this sdk that your build.gradle.kts must include:
```
dependencies {
    implementation("com.github.HaVoK9802:App_Event_Tracker_SDK:v{REPLACE WITH VERSION}")
}
```

- Find list of releases from JitPack:
https://jitpack.io/#HaVoK9802/App_Event_Tracker_SDK

## App Event Processing Contract

- ```INSTALL``` - Once Per App Installation
- ```VISIT``` - Once Per App Session
- ```SCREEN_VISIT``` - Once Per Screen per App Session
- ```ADD_TO_CART``` - Multiple times




