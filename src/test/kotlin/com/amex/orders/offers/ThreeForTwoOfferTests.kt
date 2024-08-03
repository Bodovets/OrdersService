package com.amex.orders.offers

import com.amex.orders.offer.ThreeForTwoOffer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ThreeForTwoOfferTests {
    private val offer = ThreeForTwoOffer()

    @Test
    fun `should apply 3 for 2 offer correctly for multiples of 3`() {
        val price = 0.25
        val count = 6
        val expectedCost = 4 * price

        val actualCost = offer.apply(count, price)
        assertEquals(expectedCost, actualCost)
    }

    @Test
    fun `should apply 3 for 2 offer correctly for non-multiples of 3`() {
        val price = 0.25
        val count = 5
        val expectedCost = 4 * price

        val actualCost = offer.apply(count, price)
        assertEquals(expectedCost, actualCost)
    }

    @Test
    fun `should apply 3 for 2 offer correctly for count less than 3`() {
        val price = 0.25
        val count = 2
        val expectedCost = 2 * price

        val actualCost = offer.apply(count, price)
        assertEquals(expectedCost, actualCost)
    }

    @Test
    fun `should apply 3 for 2 offer correctly for zero count`() {
        val price = 0.25
        val count = 0
        val expectedCost = 0.0

        val actualCost = offer.apply(count, price)
        assertEquals(expectedCost, actualCost)
    }
}