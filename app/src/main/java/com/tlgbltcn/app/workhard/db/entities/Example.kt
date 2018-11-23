package com.tlgbltcn.app.workhard.db.entities


import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity()
class Example {
    @PrimaryKey(autoGenerate = true) var id: Long = 0
    var exampleString: String = ""
}