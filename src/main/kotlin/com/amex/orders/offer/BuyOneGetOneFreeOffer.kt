package com.amex.orders.offer

import java.math.BigDecimal
import java.math.RoundingMode

class BuyOneGetOneFreeOffer : Offer {
    override fun apply(count: Int, price: Double): Double {
        var paidCount: Int = count / 2
        if (count % 2 != 0) {
            paidCount++
        }
        return BigDecimal(paidCount * price).setScale(2, RoundingMode.HALF_EVEN).toDouble()
    }
}