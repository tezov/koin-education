package com.tezov.koineducation.implementation

interface SimulatorProtocol {
    fun loadAndStart(program: Program)
}

class AndroidSimulator : SimulatorProtocol {
    override fun loadAndStart(program: Program) {
        when(program) {
            is Program.Android -> println("|> Android program is running")
            is Program.iOS -> error("|> try to launch iOS program in android simulator")
        }
    }
}

class iOSSimulator : SimulatorProtocol {
    override fun loadAndStart(program: Program) {
        when(program) {
            is Program.Android -> error("|> try to launch iOS program in android simulator")
            is Program.iOS -> println("|> iOS program is running")
        }
    }
}