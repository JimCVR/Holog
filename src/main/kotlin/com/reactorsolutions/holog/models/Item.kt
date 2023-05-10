package com.reactorsolutions.holog.models

import jakarta.persistence.*

@Entity
@Table(name = "item")
open class Item(

    @get:Column(name = "name")
    open var name: String,

    @get:Column(name = "description")
    open var description: String? = null,

    @get:Column(name = "author")
    open var author: String? = null,

    @get:ManyToMany(cascade = [CascadeType.ALL],mappedBy = "items")
    open var categories: MutableSet<Category> = HashSet(),

    @get:Id
    @get:GeneratedValue
    @get:Column(name = "id")
    open var id: Long? = null,
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as Item
        if (id != that.id) return false
        return true
    }

    override fun hashCode(): Int {
        return if (id != null)
            id.hashCode()
        else 0
    }
}