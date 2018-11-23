package com.tlgbltcn.app.workhard.db.repositories

import com.tlgbltcn.app.workhard.db.entities.Example

interface ExampleRepository {

    fun exampleRepo(id : Long) : Example
}