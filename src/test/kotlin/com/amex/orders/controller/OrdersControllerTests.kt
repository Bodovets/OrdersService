package com.amex.orders.controller

import com.amex.orders.exception.OrderNotFoundException
import com.amex.orders.exception.ProductNotFoundException
import com.amex.orders.model.ErrorResponse
import com.amex.orders.model.OrderItem
import com.amex.orders.model.OrderRequest
import com.amex.orders.model.OrderSummary
import com.amex.orders.service.OrderService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import kotlin.test.assertTrue

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrdersControllerTests(@LocalServerPort val port: Int, @Autowired val restTemplate: TestRestTemplate) {
    @MockBean
    private lateinit var orderService: OrderService

    @Test
    fun `should create order and return summary`() {
        val orderItems = listOf(OrderItem("apple", 2), OrderItem("orange", 3))
        val orderRequest = OrderRequest(orderItems)
        val orderSummary = OrderSummary("47a78eb4-118e-4929-a457-d53a70aac0f4", orderItems, 2 * 0.60 + 3 * 0.25)
        given(orderService.createOrder(orderRequest.items)).willReturn(orderSummary)

        val headers = HttpHeaders()
        headers.set("Content-Type", "application/json")
        val entity = HttpEntity(orderRequest, headers)

        val response = restTemplate.exchange(
            "http://localhost:$port/order",
            HttpMethod.POST,
            entity,
            OrderSummary::class.java
        )

        val expectedTotalCost = 2 * 0.60 + 3 * 0.25
        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertEquals(36, response.body?.orderId?.length)
        assertEquals(expectedTotalCost, response.body?.totalPrice)
    }

    @Test
    fun `should return bad request for unknown product`() {
        val orderItems = listOf(OrderItem("unknown", 1))
        val orderRequest = OrderRequest(orderItems)
        given(orderService.createOrder(orderRequest.items)).willThrow(ProductNotFoundException("unknown product"))
        val headers = HttpHeaders()
        headers.set("Content-Type", "application/json")
        val entity = HttpEntity(orderRequest, headers)

        val response = restTemplate.exchange(
            "http://localhost:$port/order",
            HttpMethod.POST,
            entity,
            String::class.java
        )

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
    }

    @Test
    fun `should return order when exists`() {
        val orderId = "47a78eb4-118e-4929-a457-d53a70aac0f4"
        val orderItems = listOf(OrderItem("apple", 2), OrderItem("orange", 3))
        val orderSummary = OrderSummary(orderId, orderItems, 1.0)
        given(orderService.getOrderSummary(orderId)).willReturn(orderSummary)

        val headers = HttpHeaders()
        headers.set("Content-Type", "application/json")
        val entity = HttpEntity(null, headers)

        val response = restTemplate.exchange(
            "http://localhost:$port/order/47a78eb4-118e-4929-a457-d53a70aac0f4",
            HttpMethod.GET,
            entity,
            OrderSummary::class.java
        )
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(orderId, response.body?.orderId)
        assertEquals(1.0, response.body?.totalPrice)
    }

    @Test
    fun `should return not found when doesn't exist`() {
        val orderId = "unknown-order-id"
        given(orderService.getOrderSummary(orderId)).willThrow(OrderNotFoundException(orderId))

        val headers = HttpHeaders()
        headers.set("Content-Type", "application/json")
        val entity = HttpEntity(null, headers)

        val response = restTemplate.exchange(
            "http://localhost:$port/order/unknown-order-id",
            HttpMethod.GET,
            entity,
            ErrorResponse::class.java
        )
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        assertEquals("Order '$orderId' not found", response.body?.message)
    }

    @Test
    fun `should return all orders when not empty`() {
        val orderId = "47a78eb4-118e-4929-a457-d53a70aac0f4"
        val orderItems = listOf(OrderItem("apple", 2), OrderItem("orange", 3))
        val orderSummary = OrderSummary(orderId, orderItems, 1.0)
        given(orderService.getAllOrders()).willReturn(listOf(orderSummary))

        val headers = HttpHeaders()
        headers.set("Content-Type", "application/json")
        val entity = HttpEntity(null, headers)

        val responseType = object : ParameterizedTypeReference<List<OrderSummary>>() {}
        val response = restTemplate.exchange(
            "http://localhost:$port/order",
            HttpMethod.GET,
            entity,
            responseType
        )
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(orderSummary, response.body?.get(0))
        assertEquals(1, response.body?.size)
    }

    @Test
    fun `should return an empty array when no orders`() {
        given(orderService.getAllOrders()).willReturn(listOf())

        val headers = HttpHeaders()
        headers.set("Content-Type", "application/json")
        val entity = HttpEntity(null, headers)

        val responseType = object : ParameterizedTypeReference<List<OrderSummary>>() {}
        val response = restTemplate.exchange(
            "http://localhost:$port/order",
            HttpMethod.GET,
            entity,
            responseType
        )
        assertEquals(HttpStatus.OK, response.statusCode)
        assertTrue { response.body?.isEmpty() ?: false }
    }

}