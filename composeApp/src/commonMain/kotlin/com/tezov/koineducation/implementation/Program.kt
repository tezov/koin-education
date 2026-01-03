package com.tezov.koineducation.implementation

sealed interface Program {
    fun content():Long

    fun interface iOS: Program
    fun interface Android: Program
}