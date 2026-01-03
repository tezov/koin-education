package com.tezov.koineducation.implementation

class Software(
    private val engine: EngineProtocol,
    private val simulator: SimulatorProtocol,
) {

    fun build(program: Program) {
        engine.build(program)
    }

    fun launch(program: Program) {
        simulator.loadAndStart(program)
    }
}