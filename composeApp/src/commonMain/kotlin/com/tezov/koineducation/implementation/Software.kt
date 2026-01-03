package com.tezov.koineducation.implementation

class Software(
    private val engine: Engine,
    private val simulator: Simulator
) {

    fun build(program: Program) {
        engine.build(program)
    }

    fun launch(program: Program) {
        simulator.loadAndStart(program)
    }
}