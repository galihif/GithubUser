package com.giftech.githubuser

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.giftech.githubuser.model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class MainViewModel: ViewModel() {
    val listUsers = MutableLiveData<ArrayList<User>>()
    val user = MutableLiveData<User>()
    
    companion object{
        val token = "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"
    }

    fun getUsers(query:String):LiveData<ArrayList<User>>{
        val listItems = ArrayList<User>()

        val url = "https://api.github.com/search/users?q=$query"

        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token $token")
        client.addHeader("User-Agent", "request")

        client.get(url, object : AsyncHttpResponseHandler(){
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                val result = String(responseBody!!)
                Log.d("GET", result)
                try {
                    val responseObject = JSONObject(result)
                    val items = responseObject.getJSONArray("items")

                    for (i in 0 until items.length()){
                        val userItem = items.getJSONObject(i)
                        val user = User()
                        user.userName = userItem.getString("login")
                        user.avatar = userItem.getString("avatar_url")
                        listItems.add(user)
                    }
                    listUsers.postValue(listItems)
                } catch (e:Exception){
                    Log.d("E", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                Log.d("MainonFailure", error?.message.toString())
            }

        })
        return listUsers
    }

    fun getUserDetail(username:String):LiveData<User>{
        val userData = User()

        val url = "https://api.github.com/users/$username"

        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token $token")
        client.addHeader("User-Agent", "request")

        client.get(url, object : AsyncHttpResponseHandler(){
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                val result = String(responseBody!!)
                Log.d("GET", result)
                try {
                    val responseObject = JSONObject(result)
                    userData.name = responseObject.getString("name")
                    userData.userName = responseObject.getString("login")
                    userData.avatar = responseObject.getString("avatar_url")
                    userData.following = responseObject.getInt("following")
                    userData.followers = responseObject.getInt("followers")
                    userData.company = responseObject.getString("company")
                    userData.location = responseObject.getString("location")
                    userData.repository = responseObject.getInt("public_repos")

                    user.postValue(userData)

                } catch (e:Exception){
                    Log.d("E", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                Log.d("MainonFailure", error?.message.toString())
            }

        })
        return user
    }
}