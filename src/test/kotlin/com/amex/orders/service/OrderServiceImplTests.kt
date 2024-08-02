package com.amex.orders.service

import com.amex.orders.exception.ProductNotFoundException
import com.amex.orders.model.OrderItem
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class OrderServiceImplTests {
    @Autowired
    private lateinit var orderService: OrderServiceImpl

    @Test
    fun `should calculate total cost correctly for valid order`() {
        val orderItems = listOf(OrderItem("apple", 2), OrderItem("orange", 3))
        val orderSummary = orderService.createOrder(orderItems)

        val expectedTotalCost = 2 * 0.60 + 3 * 0.25
        assertEquals(orderItems, orderSummary.items)
        assertEquals(expectedTotalCost, orderSummary.totalPrice)
    }

    @Test
    fun `should throw ProductNotFoundException for unknown product`() {
        val orderItems = listOf(OrderItem("potato", 1))

        val exception = assertThrows<ProductNotFoundException> {
            orderService.createOrder(orderItems)
        }

        assertEquals("Product 'potato' not found", exception.message)
    }

}