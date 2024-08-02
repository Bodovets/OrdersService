package com.amex.orders.controller

import com.amex.orders.model.OrderItem
import com.amex.orders.model.OrderSummary
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrdersControllerTests(@LocalServerPort val port: Int, @Autowired val restTemplate: TestRestTemplate) {
    @Test
    fun `should create order and return summary`() {
        val orderItems = listOf(OrderItem("apple", 2), OrderItem("orange", 3))
        val headers = HttpHeaders()
        headers.set("Content-Type", "application/json")
        val entity = HttpEntity(orderItems, headers)

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
        val headers = HttpHeaders()
        headers.set("Content-Type", "application/json")
        val entity = HttpEntity(orderItems, headers)

        val response = restTemplate.exchange(
            "http://localhost:$port/order",
            HttpMethod.POST,
            entity,
            String::class.java
        )

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
    }

}