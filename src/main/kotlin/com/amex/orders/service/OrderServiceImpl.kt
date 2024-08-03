package com.amex.orders.service

import com.amex.orders.exception.ProductNotFoundException
import com.amex.orders.model.OrderItem
import com.amex.orders.model.OrderSummary
import com.amex.orders.offer.BuyOneGetOneFreeOffer
import org.springframework.stereotype.Service
import java.util.*

@Service
class OrderServiceImpl() : OrderService {
    private val pricingMap = mapOf(
        "apple" to 0.6,
        "orange" to 0.25
    )

    private val offers = mapOf(
        "apple" to BuyOneGetOneFreeOffer()
    )

    override fun createOrder(orderItems: List<OrderItem>): OrderSummary {
        val orderId = UUID.randomUUID().toString()
        var totalPrice = 0.0
        for (item in orderItems) {
            val itemName = item.name.lowercase()
            val price = pricingMap[itemName] ?: throw ProductNotFoundException(item.name)
            val totalItemPrice = offers[itemName]?.apply(item.quantity, price) ?: (item.quantity * price)
            totalPrice += totalItemPrice
        }

        return OrderSummary(orderId, orderItems, totalPrice)
    }

    private fun OrderItem.calculateCost(): Double {
        return pricingMap[this.name.lowercase()]?.let { price -> price * this.quantity }
            ?: throw ProductNotFoundException(this.name)
    }
}