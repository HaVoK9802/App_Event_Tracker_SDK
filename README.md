
# App Event Tracker Simulator

In simple words, the flow goes like this, an event is triggered by the host app via the exposed api ```trackEvent(json: String)```, the json object you passed is validated and the event is queued in a Room DB (to persist across no network, power off, process death) and dispatched immediately as a work request to be processed via the WorkManager (to guarantee that work is completed across failures and constraints, via the various retry policies it offers). Processed Events are stored in another Room DB to mimic a server that has recorded the events.

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
- ```PURCHASE``` - Multiple times

# Important functions and classes

#### The class that allows user to manage the SDK
```
AppEventTracker
```
- Initialize SDK via ```initializeTracker(context: Context)``` companion method
- Do not try to access ```getInstance(): AppEventTracker``` before the initialization step

#### The function exposed via ```AppEventTracker``` that tracks events  
```
trackEvent(json: String)
```
- Every JSON object included in the event must contain a ```"event_type"``` key and a value that corresponds to ```AppEventType```
- ```SCREEN_VISIT``` events must additionally included a ```"screen_name"```key and value

#### Check out all the domain models of the SDK

#### ```SeissionManager``` to spin up a new sessionId for each app visit

#### Checkout the ```AppEventsRepoImpl``` to find out how events are queued (in ```LocalAppEventsDatabase```) up and scheduled (via ```EventUploadScheduler```) for processing

#### The events are processed individually in a ```EventUploadWorker``` and from there each event under goes a random 1-5s delay and a failure rate of 20% simulating a network call, all taken care of by a ```RemoteDataSource```

#### Processed events end up inside ```AppEventsDatabase``` mimicing a server that stores crucial events for analytics

#### Both processed and unprocessed events are exposed to the sample app via ```getProcessedEvents(): Flow<List<AppEvent>>``` and ```getUnprocessedEvents(): Flow<List<AppEventWithStatus>>``` (just for visualizing analytics, otherwise not to be done)

#### A manual flush ```destroyTracker()``` to release all dependencies of ```AppEventTracker```







