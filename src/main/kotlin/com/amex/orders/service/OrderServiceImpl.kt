package com.amex.orders.service

import com.amex.orders.exception.ProductNotFoundException
import com.amex.orders.model.OrderItem
import com.amex.orders.model.OrderSummary
import org.springframework.stereotype.Service
import java.util.*

@Service
class OrderServiceImpl : OrderService {
    private val pricingMap = mapOf(
        "apple" to 0.6,
        "orange" to 0.25
    )

    override fun createOrder(orderItems: List<OrderItem>): OrderSummary {
        val orderId = UUID.randomUUID().toString()
        val totalPrice = orderItems.sumOf { it.calculateCost() }

        return OrderSummary(orderId, orderItems, totalPrice)
    }

    private fun OrderItem.calculateCost(): Double {
        return pricingMap[this.name.lowercase()]?.let { price -> price * this.quantity }
            ?: throw ProductNotFoundException(this.name)
    }
}