package com.example.travelwriter.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "drafts")
data class Article(
    @PrimaryKey
    var id: Int = kotlin.math.abs((0..999999999999).random()).toInt(),

    @ColumnInfo(name = "author")
    var author: String = "",

    @ColumnInfo(name = "title")
    var title: String = "",

    @ColumnInfo(name = "body")
    var body: String = ""
)