package com.giftech.githubuser.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.giftech.githubuser.MainViewModel
import com.giftech.githubuser.adapter.UserAdapter
import com.giftech.githubuser.databinding.FragmentFollowingBinding
import com.giftech.githubuser.model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class FollowingFragment(username:String) : Fragment() {

    private var _binding:FragmentFollowingBinding? = null
    private val binding get() = _binding!!
    
    private val username = username
    private var listFollowing = ArrayList<User>()
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFollowingBinding.inflate(inflater, container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getFollowing()
    }

    fun getFollowing(){
        listFollowing.clear()

        val url = "https://api.github.com/users/$username/following"
        val token = MainViewModel.token

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
                        listFollowing.add(user)
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
        binding.rvFollowing.layoutManager = LinearLayoutManager(requireContext())
        val listFollowingAdapter = UserAdapter(listFollowing)
        binding.rvFollowing.adapter = listFollowingAdapter

        listFollowingAdapter.setOnItemCallback(object : UserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: User) {
                Toast.makeText(activity,data.userName,Toast.LENGTH_SHORT).show()
            }
        })
    }

}