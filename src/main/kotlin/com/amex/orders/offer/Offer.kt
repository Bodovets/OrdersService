package com.amex.orders.offer

interface Offer {
    fun apply(count: Int, price: Double): Double
}