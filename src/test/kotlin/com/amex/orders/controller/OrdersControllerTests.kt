package com.amex.orders.controller

import com.amex.orders.exception.ProductNotFoundException
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
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

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
        assertEquals(HttpStatus.OK, response.statusCode)
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

}