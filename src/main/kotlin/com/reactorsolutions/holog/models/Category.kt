package com.reactorsolutions.holog.models

import jakarta.persistence.*

@Entity
@Table(name = "category")
data class Category(
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,

    @Column(name = "name")
    var name: String?,

    @Column(name = "iconId")
    var iconId: Int?,

    )