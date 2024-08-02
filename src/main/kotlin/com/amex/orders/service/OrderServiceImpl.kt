package com.amex.orders.service

import com.amex.orders.model.OrderItem
import com.amex.orders.model.OrderSummary
import org.springframework.stereotype.Service

@Service
class OrderServiceImpl: OrderService {
    override fun createOrder(orderItems: List<OrderItem>): OrderSummary {
        TODO("Not yet implemented")
    }
}