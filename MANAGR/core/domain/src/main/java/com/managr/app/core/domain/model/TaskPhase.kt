package com.managr.app.core.domain.model

/**
 * Task phase in the release workflow
 */
enum class TaskPhase(val order: Int, val displayName: String) {
    PRE_PRODUCTION(1, "Pre-Production"),
    PRODUCTION(2, "Production"),
    DISTRIBUTION(3, "Distribution"),
    PROMOTION(4, "Promotion"),
    POST_RELEASE(5, "Post-Release");

    companion object {
        /**
         * Get phase by order
         */
        fun fromOrder(order: Int): TaskPhase? = entries.find { it.order == order }

        /**
         * Get all phases in order
         */
        fun orderedPhases(): List<TaskPhase> = entries.sortedBy { it.order }
    }
}
