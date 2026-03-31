package com.tezov.store.shared.domain

import org.koin.core.annotation.Factory

@Factory
class PlatformDomain : PlatformDomainProtocol {

    override fun description() = "from ios"

}