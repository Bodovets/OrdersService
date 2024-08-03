package com.amex.orders.exception

class ProductNotFoundException(productName: String) : RuntimeException("Product '$productName' not found")