package org.fly.eventbus

enum class ActiveState {
    CREATED, // [onCreate] -> [onStop]
    STARTED, // [onStart] -> [onPause]
    RESUMED, // [onResume]
    DESTROYED // [onDestroy]
}