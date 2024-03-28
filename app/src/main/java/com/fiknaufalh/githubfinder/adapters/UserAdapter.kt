package com.fiknaufalh.githubfinder.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fiknaufalh.githubfinder.R
import com.fiknaufalh.githubfinder.data.response.UserItem
import com.fiknaufalh.githubfinder.databinding.ItemUserCardBinding

class UserAdapter(
        private val listUser: List<UserItem>,
        private val onClickCard: (UserItem) -> Unit
    ): RecyclerView.Adapter<UserAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.MyViewHolder {
        val binding = ItemUserCardBinding.
            inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = listUser.size

    override fun onBindViewHolder(holder: UserAdapter.MyViewHolder, position: Int) {
        val user = listUser[position]
        holder.bind(user)
    }

    inner class MyViewHolder(val binding: ItemUserCardBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserItem) {

            itemView.setOnClickListener {
                onClickCard(user)
            }

            binding.userName.text = user.login
            Glide.with(binding.root)
                .load(user.avatarUrl)
                .placeholder(R.drawable.account_circle)
                .into(binding.userImage)
        }
    }

}