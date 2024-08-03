package com.amex.orders.controller

import com.amex.orders.model.OrderRequest
import com.amex.orders.model.OrderSummary
import com.amex.orders.service.OrderService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/order")
class OrdersController(private val orderService: OrderService) {
    @PostMapping
    fun createOrder(@RequestBody orderRequest: OrderRequest): OrderSummary {
        return orderService.createOrder(orderRequest.items)
    }

    @GetMapping("/{orderId}")
    fun getOrderSummary(@PathVariable orderId: String): OrderSummary {
        return orderService.getOrderSummary(orderId)
    }

    @GetMapping
    fun getAllOrders(): List<OrderSummary> {
        return orderService.getAllOrders()
    }
}