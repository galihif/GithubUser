package com.giftech.githubuser.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.giftech.githubuser.adapter.UserAdapter
import com.giftech.githubuser.databinding.FragmentFollowersBinding
import com.giftech.githubuser.model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class FollowersFragment(username:String) : Fragment() {

    private var _binding:FragmentFollowersBinding? = null
    private val binding get() = _binding!!

    private val username = username
    private var listFollowers = ArrayList<User>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFollowersBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getFollowers()
    }

    fun getFollowers(){
        listFollowers.clear()

        val url = "https://api.github.com/users/$username/followers"
        val token = "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"

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
                    val responseArray = JSONArray(result)
                    for (i in 0 until  responseArray.length()){
                        val userItem = responseArray.getJSONObject(i)
                        val user = User()
                        user.userName = userItem.getString("login")
                        user.avatar = userItem.getString("avatar_url")
                        listFollowers.add(user)
                    }
                    showRecycleList()
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
                Log.d("onFailure", error?.message.toString())
            }

        })
    }

    private fun showRecycleList() {
        binding.rvFollowers.layoutManager = LinearLayoutManager(requireContext())
        val listFollowersAdapter = UserAdapter(listFollowers)
        binding.rvFollowers.adapter = listFollowersAdapter
    }
}