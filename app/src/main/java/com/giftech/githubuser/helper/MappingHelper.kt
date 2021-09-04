package com.giftech.githubuser.helper

import android.database.Cursor
import com.giftech.githubuser.db.DatabaseContract.UserColumns
import com.giftech.githubuser.model.User

object MappingHelper {

    fun mapCursorToArrayList(userCursor: Cursor?): ArrayList<User>{
        val userList = ArrayList<User>()
        userCursor?.apply {
            while (moveToNext()){
                val name = getString(getColumnIndexOrThrow(UserColumns.NAME))
                val username = getString(getColumnIndexOrThrow(UserColumns.USERNAME))
                val avatar = getString(getColumnIndexOrThrow(UserColumns.AVATAR))
//                val company = getString(getColumnIndexOrThrow(UserColumns.COMPANY))
//                val location = getString(getColumnIndexOrThrow(UserColumns.LOCATION))
//                val repository = getInt(getColumnIndexOrThrow(UserColumns.REPOSITORY))
//                val following = getInt(getColumnIndexOrThrow(UserColumns.FOLLOWING))
//                val followers = getInt(getColumnIndexOrThrow(UserColumns.FOLLOWERS))
                val user = User(name,username,avatar)
                userList.add(user)
            }
        }
        return userList
    }

    fun mapCursorToObject(userCursor: Cursor?):User{
        var user = User()
        userCursor?.apply {
            moveToFirst()
            val name = getString(getColumnIndexOrThrow(UserColumns.NAME))
            val username = getString(getColumnIndexOrThrow(UserColumns.USERNAME))
            val avatar = getString(getColumnIndexOrThrow(UserColumns.AVATAR))
            user = User(name,username,avatar)
        }
        return user
    }

}