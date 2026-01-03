package com.tezov.koineducation.implementation

class Engine(
    private val fuel: FuelStorage
) {

    private val powerPerUnit: Int = 150

    fun build(program: Program) {
        println("|> engine building")
        fuel.burn(powerPerUnit * program.content())
    }
}
