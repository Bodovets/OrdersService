package com.amex.orders.service

import com.amex.orders.exception.OrderNotFoundException
import com.amex.orders.exception.ProductNotFoundException
import com.amex.orders.model.OrderItem
import com.amex.orders.model.OrderSummary
import com.amex.orders.repository.OrderRepo
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

@SpringBootTest
class OrderServiceImplTests {
    @Autowired
    private lateinit var orderService: OrderServiceImpl

    @MockBean
    private lateinit var orderRepository: OrderRepo

    @Test
    fun `should calculate total cost correctly for valid order`() {
        val orderItems = listOf(OrderItem("apple", 3), OrderItem("orange", 3))
        val orderSummary = orderService.createOrder(orderItems)

        val expectedTotalCost = 2 * 0.60 + 2 * 0.25
        assertEquals(orderItems, orderSummary.items)
        assertEquals(expectedTotalCost, orderSummary.totalPrice)
    }

    @Test
    fun `should calculate total cost correctly for valid order with active offer for b1g1 free`() {
        val orderItems = listOf(OrderItem("apple", 2), OrderItem("orange", 3))
        val orderSummary = orderService.createOrder(orderItems)

        val expectedTotalCost = 1 * 0.60 + 2 * 0.25
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

    @Test
    fun `should return order summary when order exists`() {
        val orderId = "existing-id"
        val expectedOrder = OrderSummary(orderId, listOf(OrderItem("apple", 2)), 1.2)
        given(orderRepository.findById(orderId)).willReturn(expectedOrder)

        val result = orderService.getOrderSummary(orderId)

        assertEquals(expectedOrder, result)
    }

    @Test
    fun `should throw OrderNotFoundException when order does not exist`() {
        val orderId = "non-existing-id"
        given(orderRepository.findById(orderId)).willReturn(null)

        assertThrows<OrderNotFoundException> {
            orderService.getOrderSummary(orderId)
        }
    }

    @Test
    fun `should return all orders`() {
        val order1 = OrderSummary("id-1", listOf(OrderItem("apple", 2)), 1.2)
        val order2 = OrderSummary("id-2", listOf(OrderItem("orange", 3)), 0.75)
        val orders = listOf(order1, order2)
        given(orderRepository.findAll()).willReturn(orders)

        val result = orderService.getAllOrders()

        assertEquals(orders, result)
    }

    @Test
    fun `should return empty list when no orders are present`() {
        given(orderRepository.findAll()).willReturn(emptyList())

        val result = orderService.getAllOrders()

        assertTrue(result.isEmpty())
    }
}