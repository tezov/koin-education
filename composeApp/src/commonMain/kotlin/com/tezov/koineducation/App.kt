package com.tezov.koineducation

import androidx.compose.runtime.Composable
import com.tezov.koineducation.implementation.FuelStorage
import com.tezov.koineducation.implementation.Program
import com.tezov.koineducation.implementation.Software
import com.tezov.koineducation.implementation.Engine
import com.tezov.koineducation.implementation.Simulator
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    val fuelStorage = FuelStorage(initialQuantity = 1000)
    val engine = Engine(fuel = fuelStorage)
    val simulator = Simulator()
    val software = Software(
        engine = engine,
        simulator = simulator
    )

    with(Program { 5L }) {
        software.build(this)
        software.launch(this)
    }
}
