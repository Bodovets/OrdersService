package com.amex.orders.controller

import com.amex.orders.model.OrderRequest
import com.amex.orders.model.OrderSummary
import com.amex.orders.service.OrderService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/order")
class OrdersController(private val orderService: OrderService) {
    @PostMapping
    fun createOrder(@RequestBody orderRequest: OrderRequest): OrderSummary {
        return orderService.createOrder(orderRequest.items);
    }
}