package com.amex.orders.model

data class OrderSummary(val orderId: String, val items: List<OrderItem>, val totalPrice: Double)