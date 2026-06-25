package com.jacks.pizzas.core.domain.util

sealed interface DataError: Error {
    enum class Network: DataError {
        NO_INTERNET,
        SERIALIZATION,
        SERVER_SIDE
    }
}