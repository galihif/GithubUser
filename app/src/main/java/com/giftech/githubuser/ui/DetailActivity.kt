package com.giftech.githubuser.ui

import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.giftech.githubuser.MainViewModel
import com.giftech.githubuser.R
import com.giftech.githubuser.adapter.SectionsPagerAdapter
import com.giftech.githubuser.databinding.ActivityDetailBinding
import com.giftech.githubuser.db.DatabaseContract.UserColumns.Companion.AVATAR
import com.giftech.githubuser.db.DatabaseContract.UserColumns.Companion.FAVOURITE_USER_URI
import com.giftech.githubuser.db.DatabaseContract.UserColumns.Companion.NAME
import com.giftech.githubuser.db.DatabaseContract.UserColumns.Companion.USERNAME
import com.giftech.githubuser.db.UserHelper
import com.giftech.githubuser.helper.MappingHelper
import com.giftech.githubuser.model.User
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {

    companion object{
        const val USER_DATA = "user_data"
        private val TAB_TITLES = arrayListOf<String>(
            "FOLLOWERS",
            "FOLLOWING"
        )
    }

    private lateinit var binding: ActivityDetailBinding
    private lateinit var mainViewModel: MainViewModel

    private var isFavourited = false

    private lateinit var uriWithUsername:Uri
    private lateinit var userHelper: UserHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)

        showLoading(true)
        userHelper = UserHelper.getInstance(this)

        var userData = intent.getParcelableExtra<User>(USER_DATA) as User

        mainViewModel.getUserDetail(userData.userName).observe(this, {user ->
            userData = user
            showUserDetail(userData)
            checkIsFavourited(userData.userName)
        })

        binding.content.btnFavourite.setOnClickListener {
            isFavourited = !isFavourited
            setFavouriteDrawable()
            if (isFavourited){
                val values = ContentValues()
                values.put(NAME, userData.name)
                values.put(USERNAME, userData.userName)
                values.put(AVATAR, userData.avatar
                )

                contentResolver.insert(FAVOURITE_USER_URI, values)

                Toast.makeText(this, "${userData.userName} Ditambah ke favorit", Toast.LENGTH_SHORT).show()
            }else{
                uriWithUsername = Uri.parse(FAVOURITE_USER_URI.toString()+"/"+userData.userName)
                contentResolver.delete(uriWithUsername,null,null)

                Toast.makeText(this, "${userData.userName} dihapus dari favorit", Toast.LENGTH_SHORT).show()
            }
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = userData.userName
        binding.content.viewPager.adapter = sectionsPagerAdapter

        TabLayoutMediator(binding.content.tabLayout, binding.content.viewPager){tab, position ->
            tab.text = TAB_TITLES[position]
        }.attach()
    }

    private fun showUserDetail(user: User){
        Glide.with(this)
            .load(user.avatar)
            .centerCrop()
            .into(binding.content.ivAvatar)

        supportActionBar?.title = user.name
        binding.content.tvName.text = user.name
        binding.content.tvUsername.text = user.userName
        binding.content.tvCompany.text = user.company
        binding.content.tvLocation.text = user.location
        binding.content.tvRepo.text = user.repository.toString()
        showLoading(false)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.loading.visibility = View.VISIBLE
            binding.content.root.visibility = View.GONE
        } else {
            binding.loading.visibility = View.GONE
            binding.content.root.visibility = View.VISIBLE
        }
    }

    private fun setFavouriteDrawable(){
        if(isFavourited){
            binding.content.btnFavourite.setImageResource(R.drawable.ic_baseline_bookmark_filled_white_24)
        } else {
            binding.content.btnFavourite.setImageResource(R.drawable.ic_baseline_bookmark_border_white_24)
        }
    }

    private fun checkIsFavourited(username:String){
        GlobalScope.launch(Dispatchers.Main) {
            val userHelper = UserHelper.getInstance(applicationContext)
            userHelper.open()

            val deferredList = async(Dispatchers.IO) {
                val cursor = userHelper.queryByUsername(username)
                MappingHelper.mapCursorToArrayList(cursor)
            }

            val list = deferredList.await()

            isFavourited = list.size > 0
            setFavouriteDrawable()
            userHelper.close()
        }
    }
}