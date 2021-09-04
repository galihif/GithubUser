package com.giftech.githubuser.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.giftech.githubuser.db.DatabaseContract.AUTHORITY
import com.giftech.githubuser.db.DatabaseContract.UserColumns.Companion.FAVOURITE_USER_URI
import com.giftech.githubuser.db.DatabaseContract.UserColumns.Companion.TABLE_NAME
import com.giftech.githubuser.db.UserHelper

class UserProvider: ContentProvider() {

    companion object{
        private const val USER_ALL = 1
        private const val USER_BY_USERNAME = 2
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var userHelper: UserHelper

        init {
            // com.giftech.githubuser/user
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, USER_ALL)

            // com.giftech.githubuser/user/galihif
            sUriMatcher.addURI(AUTHORITY,"$TABLE_NAME/#", USER_BY_USERNAME)
        }
    }

    override fun onCreate(): Boolean {
        userHelper = UserHelper.getInstance(context as Context)
        userHelper.open()
        return false
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        return userHelper.queryAll()
    }

    override fun getType(p0: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, contentValues: ContentValues?): Uri? {
        val added:Long = when(USER_ALL){
            sUriMatcher.match(uri) -> userHelper.insert(contentValues)
            else -> 0
        }
        context?.contentResolver?.notifyChange(FAVOURITE_USER_URI, null)

        return Uri.parse("$FAVOURITE_USER_URI/$added")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
//        val deleted:Int = when(sUriMatcher.match(uri)){
//            USER_BY_USERNAME -> userHelper.deleteByUsername(uri.lastPathSegment.toString())
//            else -> 0
//        }
        val deleted = userHelper.deleteByUsername(uri.lastPathSegment.toString())
        context?.contentResolver?.notifyChange(FAVOURITE_USER_URI, null)
        return deleted
    }

    override fun update(uri: Uri, contentValues: ContentValues?,
                        selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }


}