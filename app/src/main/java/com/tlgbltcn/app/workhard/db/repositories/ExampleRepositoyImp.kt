package com.tlgbltcn.app.workhard.db.repositories

import com.tlgbltcn.app.workhard.db.AppDatabase
import com.tlgbltcn.app.workhard.db.entities.Example
import javax.inject.Inject

class ExampleRepositoyImp @Inject constructor(var db : AppDatabase ) : ExampleRepository {


    override fun exampleRepo(id: Long): Example {
        return db.exampleDao().getExample(id)
    }
}