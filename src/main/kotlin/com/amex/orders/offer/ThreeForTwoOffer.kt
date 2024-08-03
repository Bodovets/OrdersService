package com.amex.orders.offer

class ThreeForTwoOffer : Offer {
    override fun apply(count: Int, price: Double): Double {
        if (count < 3) {
            return count * price
        }
        var paidCount = (count / 3) * 2 + (count % 3)
        return paidCount * price
    }
}