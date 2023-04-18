package com.reactorsolutions.holog.models

import jakarta.persistence.*

@Entity
@Table(name = "item")
data class Item(
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,

    @Column(name = "name")
    var name: String,

    @Column(name = "description")
    var description: String,


    @Column(name = "author")
    var author: String,

    /*@ManyToOne
    @JoinColumn(name = "categoryId")
    var category: Category*/
) {
}