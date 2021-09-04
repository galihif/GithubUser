package com.giftech.githubuser.ui

import android.content.Intent
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.giftech.githubuser.adapter.UserAdapter
import com.giftech.githubuser.databinding.ActivityFavouriteBinding
import com.giftech.githubuser.db.DatabaseContract.UserColumns.Companion.FAVOURITE_USER_URI
import com.giftech.githubuser.db.UserHelper
import com.giftech.githubuser.helper.MappingHelper
import com.giftech.githubuser.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavouriteActivity : AppCompatActivity() {

    private lateinit var binding : ActivityFavouriteBinding
    private var listFavourites = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavouriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Favourites"



        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object: ContentObserver(handler){
            override fun onChange(selfChange: Boolean) {
                loadUsersAsync()
            }
        }

        contentResolver.registerContentObserver(FAVOURITE_USER_URI, true, myObserver)

        loadUsersAsync()
    }

    private fun loadUsersAsync() {
        GlobalScope.launch(Dispatchers.Main){
            val userHelper = UserHelper.getInstance(applicationContext)
            userHelper.open()

            val deferredUser = async(Dispatchers.IO){
//                val cursor = contentResolver.query(
//                    FAVOURITE_USER_URI,
//                    null,
//                    null,
//                    null,
//                    null,
//                )
                val cursor = userHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }

            listFavourites.clear()
            val users = deferredUser.await()
            listFavourites.addAll(users)
            showRecycleList()
            userHelper.close()
        }
    }

    private fun showRecycleList(){
        binding.rvFavourite.layoutManager = LinearLayoutManager(this)
        val listFavouritesAdapter = UserAdapter(listFavourites)
        binding.rvFavourite.adapter = listFavouritesAdapter

        listFavouritesAdapter.setOnItemCallback(object : UserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: User) {
                goToDetail(data)
            }

        })
    }

    private fun goToDetail(user: User) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.USER_DATA,user)
        startActivity(intent)
    }
}