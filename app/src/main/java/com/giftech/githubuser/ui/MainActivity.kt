package com.giftech.githubuser.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.giftech.githubuser.MainViewModel
import com.giftech.githubuser.R
import com.giftech.githubuser.adapter.UserAdapter
import com.giftech.githubuser.databinding.ActivityMainBinding
import com.giftech.githubuser.model.User

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    private var listUser = arrayListOf<User>()

    private var querySearch = "galih"

    private var loading = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)
        getDataFromViewModel(querySearch)
    }

    fun getDataFromViewModel(query:String){
        listUser.clear()
        showLoading(true)
        mainViewModel.getUsers(query).observe(this, {listUsers ->
            if(listUsers!=null){
                listUser = listUsers
                showRecycleList()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = "Search"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                querySearch = query.toString()
                getDataFromViewModel(query!!)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.favourite -> {
                startActivity(Intent(this, FavouriteActivity::class.java))
            }
            else -> return true
        }
        return true
    }

    private fun showRecycleList() {
        binding.rvUser.layoutManager = LinearLayoutManager(this)
        val listUserAdapter = UserAdapter(listUser)
        binding.rvUser.adapter = listUserAdapter
        listUserAdapter.setOnItemCallback(object : UserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: User) {
                goToDetail(data)
            }
        })
        Handler(Looper.getMainLooper()).postDelayed({
            showLoading(false)
        }, 3000)
    }

    private fun showLoading(state:Boolean) {
        mainViewModel.setLoading(state)
        mainViewModel.getLoading().observe(this, { loading ->
            if (loading) {
                binding.loading.visibility = View.VISIBLE
                binding.rvUser.visibility = View.GONE
            } else {
                binding.loading.visibility = View.GONE
                binding.rvUser.visibility = View.VISIBLE
            }
        })
    }

    private fun goToDetail(user: User) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.USER_DATA,user)
        startActivity(intent)
    }
}