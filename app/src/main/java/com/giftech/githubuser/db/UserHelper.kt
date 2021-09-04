package com.giftech.githubuser.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import com.giftech.githubuser.db.DatabaseContract.UserColumns.Companion.USERNAME
import com.giftech.githubuser.db.DatabaseContract.UserColumns.Companion._ID

class UserHelper(context: Context) {

    private val databaseHelper = DatabaseHelper(context)
    private lateinit var database:SQLiteDatabase

    companion object{
        private const val DATABASE_TABLE = DatabaseContract.UserColumns.TABLE_NAME

        private var INSTANCE:UserHelper? = null
        fun getInstance(context: Context):UserHelper =
            INSTANCE ?: synchronized(this){
                INSTANCE ?: UserHelper(context)
            }
    }

    @Throws(SQLException::class)
    fun open(){
        database = databaseHelper.writableDatabase
    }

    fun close(){
        databaseHelper.close()

        if(database.isOpen){
            database.close()
        }
    }

    fun queryAll():Cursor{
        return  database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$_ID "
        )
    }

    fun queryByUsername(username: String):Cursor{
        return database.query(
            DATABASE_TABLE,
            null,
            "$USERNAME =? ",
            arrayOf(username),
            null,
            null,
            null,
            null
        )
    }

    fun insert(values : ContentValues?):Long{
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun deleteByUsername(username:String): Int {
        return database.delete(
            DATABASE_TABLE,
            "$USERNAME =? ",
            arrayOf(username)
        )
    }

}