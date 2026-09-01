package com.example.app_event_tracker.domain.models

public enum class AppEventType(public val repetition: AppEventTriggerRepetitionType) {
    INSTALL(AppEventTriggerRepetitionType.STRICTLY_ONCE),
    VISIT(AppEventTriggerRepetitionType.ONCE_PER_SESSION),
    PURCHASE(AppEventTriggerRepetitionType.MULTIPLE),
    ADD_TO_CART(AppEventTriggerRepetitionType.MULTIPLE),
    UNKNOWN(AppEventTriggerRepetitionType.MULTIPLE)
}

public enum class AppEventTriggerRepetitionType {
    STRICTLY_ONCE,
    ONCE_PER_SESSION,
    MULTIPLE
}