package com.tezov.store.shared.data.model

import com.tezov.store.shared.domain.models.TechRoleDomainModel

enum class TechRoleDataModel {
    JUNIOR_DEVELOPER,
    SENIOR_DEVELOPER,
    STAFF_ENGINEER,
    ARCHITECT,
    PRODUCT_MANAGER,
    SCRUM_MASTER,
    DEVOPS_ENGINEER,
    QA_ENGINEER
}

fun TechRoleDataModel.toTechRoleDomainModel(): TechRoleDomainModel = when (this) {
    TechRoleDataModel.JUNIOR_DEVELOPER -> TechRoleDomainModel.JUNIOR_DEVELOPER
    TechRoleDataModel.SENIOR_DEVELOPER -> TechRoleDomainModel.SENIOR_DEVELOPER
    TechRoleDataModel.STAFF_ENGINEER -> TechRoleDomainModel.STAFF_ENGINEER
    TechRoleDataModel.ARCHITECT -> TechRoleDomainModel.ARCHITECT
    TechRoleDataModel.PRODUCT_MANAGER -> TechRoleDomainModel.PRODUCT_MANAGER
    TechRoleDataModel.SCRUM_MASTER -> TechRoleDomainModel.SCRUM_MASTER
    TechRoleDataModel.DEVOPS_ENGINEER -> TechRoleDomainModel.DEVOPS_ENGINEER
    TechRoleDataModel.QA_ENGINEER -> TechRoleDomainModel.QA_ENGINEER
}

fun TechRoleDomainModel.toTechRoleDataModel(): TechRoleDataModel = when (this) {
    TechRoleDomainModel.JUNIOR_DEVELOPER -> TechRoleDataModel.JUNIOR_DEVELOPER
    TechRoleDomainModel.SENIOR_DEVELOPER -> TechRoleDataModel.SENIOR_DEVELOPER
    TechRoleDomainModel.STAFF_ENGINEER -> TechRoleDataModel.STAFF_ENGINEER
    TechRoleDomainModel.ARCHITECT -> TechRoleDataModel.ARCHITECT
    TechRoleDomainModel.PRODUCT_MANAGER -> TechRoleDataModel.PRODUCT_MANAGER
    TechRoleDomainModel.SCRUM_MASTER -> TechRoleDataModel.SCRUM_MASTER
    TechRoleDomainModel.DEVOPS_ENGINEER -> TechRoleDataModel.DEVOPS_ENGINEER
    TechRoleDomainModel.QA_ENGINEER -> TechRoleDataModel.QA_ENGINEER
}