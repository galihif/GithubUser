package com.giftech.githubuser.db

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {

    const val AUTHORITY = "com.giftech.githubuser"
    const val SCHEME = "content"

    class UserColumns : BaseColumns{
        companion object{
            const val TABLE_NAME = "user"
            const val _ID = "_id"
            const val NAME = "name"
            const val USERNAME = "username"
            const val AVATAR = "avatar"
//            const val COMPANY = "avatar"
//            const val LOCATION = "avatar"
//            const val REPOSITORY = "avatar"
//            const val FOLLOWING = "avatar"
//            const val FOLLOWERS = "avatar"

            val FAVOURITE_USER_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }

}