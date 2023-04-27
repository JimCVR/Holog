package com.reactorsolutions.holog.models

import jakarta.persistence.*

@Entity
@Table(name = "item")
data class Item(

    @Column(name = "name")
    var name: String,

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "items")
    var categories: MutableSet<Category>,

    @Column(name = "description")
    var description: String? = null,

    @Column(name = "author")
    var author: String? = null,



    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
)