package com.reactorsolutions.holog.models

import jakarta.persistence.*
import java.sql.Date

@Entity
@Table(name = "item")
open class Item(

    @get:Column(name = "name")
    open var name: String,

    @get:Column(name = "description",length = 2000)
    open var description: String? = null,

    @get:Column(name = "picture")
    open var picture: String? = null,

    @get:Column(name = "score")
    open var score: Double? = null,

    @Temporal(TemporalType.DATE)
    @get:Column(name = "date")
    open var date: Date? = null,

    @get:Column(name = "status")
    open var status: String? = null,

    @get:Column(name = "custom")
    open var custom: Boolean? = null,

    @get:ManyToOne
    @JoinColumn(name="category_id")
    open var category: Category?=null,

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