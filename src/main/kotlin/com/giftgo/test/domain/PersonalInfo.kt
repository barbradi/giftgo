package com.giftgo.test.domain

import java.util.UUID

data class PersonalInfo(
    val uuid: UUID,
    val id: String,
    val name: String,
    val likes: String,
    val transport: String,
    val avgSpeed: Double,
    val topSpeed: Double
)