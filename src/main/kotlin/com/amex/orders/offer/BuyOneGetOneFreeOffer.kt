package com.amex.orders.offer

class BuyOneGetOneFreeOffer : Offer {
    override fun apply(count: Int, price: Double): Double {
        var paidCount = count / 2
        if (count % 2 != 0) {
            paidCount++
        }
        return paidCount * price
    }
}