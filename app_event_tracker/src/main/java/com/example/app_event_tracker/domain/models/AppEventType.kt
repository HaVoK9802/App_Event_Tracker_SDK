package com.example.app_event_tracker.domain.models

public sealed class AppEventType {

    public val name: String
        get() = when (this) {
            is StrictlyOnceEvent.Install -> INSTALL
            is OncePerSessionEvent.Visit -> VISIT
            is OncePerSessionEvent.ScreenVisit -> SCREEN_VISIT
            is MultipleEvent.Purchase -> PURCHASE
            is MultipleEvent.AddToCart -> ADD_TO_CART
            is Unknown -> UNKNOWN
        }
    public companion object{

        public const val INSTALL: String = "INSTALL"
        public const val VISIT: String = "VISIT"
        public const val SCREEN_VISIT: String = "SCREEN_VISIT"
        public const val PURCHASE: String = "PURCHASE"
        public const val ADD_TO_CART: String = "ADD_TO_CART"

        public const val UNKNOWN: String = "UNKNOWN"

        public fun fromString(name: String): AppEventType {
            return when (name) {
                INSTALL -> StrictlyOnceEvent.Install
                VISIT -> OncePerSessionEvent.Visit
                SCREEN_VISIT -> OncePerSessionEvent.ScreenVisit
                PURCHASE -> MultipleEvent.Purchase
                ADD_TO_CART -> MultipleEvent.AddToCart
                else -> Unknown
            }
        }
    }


    public sealed class StrictlyOnceEvent : AppEventType() {
        public data object Install : StrictlyOnceEvent()
    }

    public sealed class OncePerSessionEvent : AppEventType() {
        public data object Visit : OncePerSessionEvent()

        public data object ScreenVisit: OncePerSessionEvent()

    }

    public sealed class MultipleEvent : AppEventType() {
        public data object Purchase : MultipleEvent()
        public data object AddToCart : MultipleEvent()
    }

    public data object Unknown : AppEventType()
}
