package com.giftech.githubuser.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.giftech.githubuser.db.DatabaseContract.UserColumns

class DatabaseHelper(context: Context) :
   SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

       companion object{
           private const val DATABASE_NAME = "githubuser"
           private const val DATABASE_VERSION = 1
           private const val SQL_CREATE_TABLE_NOTE =
               "CREATE TABLE ${UserColumns.TABLE_NAME}" +
                       " (${UserColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                       " ${UserColumns.NAME} TEXT NOT NULL," +
                       " ${UserColumns.USERNAME} TEXT NOT NULL," +
                       " ${UserColumns.AVATAR} TEXT NOT NULL)"
//                       " ${UserColumns.COMPANY} TEXT NOT NULL," +
//                       " ${UserColumns.LOCATION} TEXT NOT NULL," +
//                       " ${UserColumns.REPOSITORY} INTEGER NOT NULL," +
//                       " ${UserColumns.FOLLOWERS} INTEGER NOT NULL," +
//                       " ${UserColumns.FOLLOWING} INTEGER NOT NULL)"
       }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_TABLE_NOTE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${UserColumns.TABLE_NAME}")
        onCreate(db)
    }
}