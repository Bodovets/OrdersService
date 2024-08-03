package com.amex.orders.repository

import com.amex.orders.model.OrderSummary
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap

@Repository
class OrderRepo {
    private val orders = ConcurrentHashMap<String, OrderSummary>()

    fun save(orderSummary: OrderSummary) {
        orders[orderSummary.orderId] = orderSummary
    }

    fun findById(orderId: String): OrderSummary? {
        return orders[orderId]
    }

    fun findAll(): List<OrderSummary> {
        return orders.values.toList()
    }
}