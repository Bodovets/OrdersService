package com.amex.orders.service

import com.amex.orders.exception.OrderNotFoundException
import com.amex.orders.exception.ProductNotFoundException
import com.amex.orders.model.OrderItem
import com.amex.orders.model.OrderSummary
import com.amex.orders.offer.BuyOneGetOneFreeOffer
import com.amex.orders.offer.Offer
import com.amex.orders.offer.ThreeForTwoOffer
import com.amex.orders.repository.OrderRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class OrderServiceImpl @Autowired constructor(
    private val orderRepo: OrderRepo,
    private val pricingMap: Map<String, Double> = mapOf(
        "apple" to 0.6,
        "orange" to 0.25
    ),
    private val offers: Map<String, Offer> = mapOf(
        "apple" to BuyOneGetOneFreeOffer(),
        "orange" to ThreeForTwoOffer()
    )
) : OrderService {


    override fun createOrder(orderItems: List<OrderItem>): OrderSummary {
        val orderId = UUID.randomUUID().toString()
        var totalPrice = 0.0
        for (item in orderItems) {
            val itemName = item.name.lowercase()
            val price = pricingMap[itemName] ?: throw ProductNotFoundException(item.name)
            val totalItemPrice = offers[itemName]?.apply(item.quantity, price) ?: (item.quantity * price)
            totalPrice += totalItemPrice
        }
        val orderSummary = OrderSummary(orderId, orderItems, totalPrice)
        orderRepo.save(orderSummary)
        return orderSummary
    }

    override fun getOrderSummary(orderId: String): OrderSummary {
        return orderRepo.findById(orderId) ?: throw OrderNotFoundException(orderId)
    }
}