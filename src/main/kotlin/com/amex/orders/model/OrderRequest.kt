package com.amex.orders.model

import jakarta.validation.Valid

data class OrderRequest(
    @field:Valid val items: List<OrderItem>
)
