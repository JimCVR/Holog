package com.reactorsolutions.holog.models

import jakarta.persistence.*

@Entity
@Table(name = "category")
data class Category(

    @Column(name = "name")
    var name: String,

    @Column(name = "iconId")
    var iconId: Int?=null,

    @ManyToMany(cascade = [CascadeType.PERSIST,CascadeType.MERGE], fetch = FetchType.LAZY)
    @JoinTable(
        name = "category_item",
        joinColumns = [JoinColumn(name = "category_id")],
        inverseJoinColumns = [JoinColumn(name = "item_id")]
    )
    var items: MutableSet<Item>?=null,

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long ?= null,

    )