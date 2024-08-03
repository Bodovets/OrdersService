package com.amex.orders.model

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class OrderItem(
    @field:NotBlank(message = "Item name cannot be blank")
    val name: String,
    @field:NotNull(message = "Quantity cannot be null")
    @field:Min(value = 1, message = "Quantity must be at least 1")
    val quantity: Int
)