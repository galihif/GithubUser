package com.giftech.githubuser.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.giftech.githubuser.R
import com.giftech.githubuser.databinding.ItemUserBinding
import com.giftech.githubuser.model.User

class UserAdapter (private val listUser:ArrayList<User>):
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    inner class ViewHolder (itemView: View):RecyclerView.ViewHolder(itemView){
        private var binding = ItemUserBinding.bind(itemView)
        var tvUsername = binding.tvUsername
        var ivAvatar = binding.ivAvatar
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }

    fun setOnItemCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = listUser[position]
        holder.tvUsername.text = user.userName
        Glide.with(holder.itemView)
            .load(user.avatar)
            .centerCrop()
            .into(holder.ivAvatar)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(user)
        }
    }

    override fun getItemCount(): Int {
        return listUser.size
    }
}