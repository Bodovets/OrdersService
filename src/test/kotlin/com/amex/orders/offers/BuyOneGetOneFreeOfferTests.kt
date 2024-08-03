package com.amex.orders.offers

import com.amex.orders.offer.BuyOneGetOneFreeOffer
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class BuyOneGetOneFreeOfferTests {
    private val offer = BuyOneGetOneFreeOffer()

    @Test
    fun `should calculate correct cost for 1 item`() {
        val totalCost = offer.apply(1, 0.6)

        assertEquals(0.6, totalCost)
    }
    @Test
    fun `should calculate correct cost for buy one get one free`() {
        val totalCost = offer.apply(2, 0.6)

        assertEquals(0.6, totalCost)
    }

    @Test
    fun `should calculate correct cost when 3 items for cost of 2`() {
        val totalCost = offer.apply(3, 0.6)

        assertEquals(1.2, totalCost)
    }

    @Test
    fun `should calculate correct cost when 4 items for cost of 2`() {
        val totalCost = offer.apply(4, 0.6)

        assertEquals(1.2, totalCost)
    }
}