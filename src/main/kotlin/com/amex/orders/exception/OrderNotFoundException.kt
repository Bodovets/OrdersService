package com.amex.orders.exception

class OrderNotFoundException(orderId: String) : RuntimeException("Order '$orderId' not found")