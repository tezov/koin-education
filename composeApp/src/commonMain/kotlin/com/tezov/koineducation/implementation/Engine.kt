package com.tezov.koineducation.implementation

interface EngineProtocol {
    fun build(program: Program)
}

class XcodeEngine(
    private val fuel: FuelStorageProtocol
): EngineProtocol {

    private val powerPerUnit: Int = 150

    override fun build(program: Program) {
        println("|> Xcode building")
        fuel.burn(powerPerUnit * program.content())
    }
}

class AndroidStudioEngine(
    private val fuel: FuelStorageProtocol
): EngineProtocol {

    private val powerPerUnit: Int = 1500

    override fun build(program: Program) {
        println("|> Android Studio building")
        when(program){
            is Program.Android -> {
                fuel.burn(powerPerUnit * program.content())
            }
            is Program.iOS -> {
                error("|> please use a mac os to build iOS program")
            }
        }
    }
}