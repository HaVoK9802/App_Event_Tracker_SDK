package com.example.app_event_tracker.domain.models

public sealed class AppEventType {

    public val name: String
        get() = when (this) {
            is StrictlyOnceEvent.Install -> "Install"
            is OncePerSessionEvent.Visit -> "Visit"
            is MultipleEvent.Purchase -> "Purchase"
            is MultipleEvent.AddToCart -> "AddToCart"
            is Unknown -> "Unknown"
        }
    public companion object{
        public fun fromString(name: String): AppEventType {
            return when (name) {
                "Install" -> StrictlyOnceEvent.Install
                "Visit" -> OncePerSessionEvent.Visit
                "Purchase" -> MultipleEvent.Purchase
                "AddToCart" -> MultipleEvent.AddToCart
                else -> Unknown
            }
        }
    }


    public sealed class StrictlyOnceEvent : AppEventType() {
        public data object Install : StrictlyOnceEvent()
    }

    public sealed class OncePerSessionEvent : AppEventType() {
        public data object Visit : OncePerSessionEvent()
    }

    public sealed class MultipleEvent : AppEventType() {
        public data object Purchase : MultipleEvent()
        public data object AddToCart : MultipleEvent()
    }

    public data object Unknown : AppEventType()
}
