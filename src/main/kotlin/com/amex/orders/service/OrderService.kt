package com.amex.orders.service

import com.amex.orders.model.OrderItem
import com.amex.orders.model.OrderSummary

interface OrderService {
    fun createOrder(orderItems: List<OrderItem>): OrderSummary
    fun getOrderSummary(orderId: String): OrderSummary
}