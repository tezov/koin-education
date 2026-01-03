package com.tezov.koineducation.implementation

import kotlin.math.abs

class FuelStorage(
    initialQuantity: Long
) {

    private var quantityRemaining = initialQuantity

    fun burn(quantity: Long) {
        val result = quantityRemaining - quantity
        if(result < 0) {
            error("|> not enough developers to burn, missing ${abs(result)} peoples")
        }
        quantityRemaining = result
        println("|> burned $quantity developers, remaining $quantityRemaining developers")
    }
}