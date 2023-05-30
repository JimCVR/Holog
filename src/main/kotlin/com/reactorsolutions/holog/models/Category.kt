package com.reactorsolutions.holog.models

import jakarta.persistence.*

@Entity
@Table(name = "category")
open class Category (

    @get:Column(name = "name")
    open var name: String,

    @get:Column(name = "icon_id")
    open var iconId: Int? = null,

    @get:OneToMany(mappedBy = "category", cascade = [CascadeType.ALL])
    open var items: MutableSet<Item> = HashSet(),

    @get:Column(name = "user_id")
    open var userId: String? = null,

    @get:Id
    @get:GeneratedValue
    @get:Column(name = "id")
    open var id: Long? = null,
){

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Category) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}