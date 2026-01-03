package com.tezov.koineducation

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform