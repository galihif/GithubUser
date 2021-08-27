package com.giftech.githubuser.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.giftech.githubuser.MainViewModel
import com.giftech.githubuser.adapter.SectionsPagerAdapter
import com.giftech.githubuser.databinding.ActivityDetailBinding
import com.giftech.githubuser.model.User
import com.google.android.material.tabs.TabLayoutMediator

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)

        var userData = intent.getParcelableExtra<User>(USER_DATA) as User

        mainViewModel.getUserDetail(userData.userName).observe(this, {user ->
            userData = user
            showUserDetail(userData)
        })

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = userData.userName
        binding.viewPager.adapter = sectionsPagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager){tab, position ->
            tab.text = TAB_TITLES[position]
        }.attach()
    }

    private fun showUserDetail(user: User){
        Glide.with(this)
            .load(user.avatar)
            .centerCrop()
            .into(binding.ivAvatar)

        supportActionBar?.title = user.name
        binding.tvName.text = user.name
        binding.tvUsername.text = user.userName
        binding.tvCompany.text = user.company
        binding.tvLocation.text = user.location
        binding.tvRepo.text = user.repository.toString()
    }
}