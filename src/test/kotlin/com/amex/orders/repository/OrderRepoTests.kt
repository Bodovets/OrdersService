package com.amex.orders.repository

import com.amex.orders.model.OrderItem
import com.amex.orders.model.OrderSummary
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class OrderRepoTests {
    @Autowired
    private lateinit var orderRepository: OrderRepo

    @BeforeEach
    fun cleanDatabase() {
        orderRepository.deleteAll()
    }

    @Test
    fun `should find all orders`() {
        val orderItems1 = listOf(OrderItem("apple", 2))
        val orderItems2 = listOf(OrderItem("orange", 3))
        val order1 = OrderSummary(orderId = "test-id-1", items = orderItems1, totalPrice = 1.20)
        val order2 = OrderSummary(orderId = "test-id-2", items = orderItems2, totalPrice = 0.75)

        orderRepository.save(order1)
        orderRepository.save(order2)

        val allOrders = orderRepository.findAll()
        assertEquals(2, allOrders.count())
    }

    @Test
    fun `should save and find order by id`() {
        val orderItems = listOf(OrderItem("apple", 2), OrderItem("orange", 3))
        val order = OrderSummary(orderId = "test-id", items = orderItems, totalPrice = 1.35)

        orderRepository.save(order)

        val foundOrder = orderRepository.findById(order.orderId)
        assertNotNull(foundOrder)
        assertEquals(order.orderId, foundOrder?.orderId)
        assertEquals(order.totalPrice, foundOrder?.totalPrice)
        assertEquals(order.items.size, foundOrder?.items?.size)
    }
}