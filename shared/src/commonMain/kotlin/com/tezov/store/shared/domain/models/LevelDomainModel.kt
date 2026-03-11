package com.tezov.store.shared.domain.models

import kotlin.jvm.JvmInline

@JvmInline
value class LevelDomainModel(val value: Int) {
    init {
        require(value in 1..10)
    }
}