package com.fiknaufalh.githubfinder.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fiknaufalh.githubfinder.R
import com.fiknaufalh.githubfinder.data.response.UserItem
import com.fiknaufalh.githubfinder.database.FavoriteUser
import com.fiknaufalh.githubfinder.databinding.ItemUserCardBinding

class UserAdapter<T>(
        private val listUser: List<T>?,
        private val onClickCard: (T) -> Unit,
        private val onClickFav: (T) -> Unit
    ): RecyclerView.Adapter<UserAdapter<T>.MyViewHolder>() {

    private var favUsers: List<FavoriteUser> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemUserCardBinding.
            inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = listUser!!.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = listUser?.get(position)
        if (user != null) {
            holder.bind(user)
        }
    }

    fun updateFavoriteUsers(users: List<FavoriteUser>) {
        favUsers = users
        notifyDataSetChanged()
    }

    inner class MyViewHolder(val binding: ItemUserCardBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(user: T) {

            itemView.setOnClickListener {
                onClickCard(user)
            }

            binding.ivFavorite.setOnClickListener {
                onClickFav(user)
            }

            when(user) {
                is UserItem -> {
                    binding.userName.text = user.login
                }
                is FavoriteUser -> {
                    binding.userName.text = user.username
                }
            }

            if (favUsers.isNotEmpty()) {
                val isFavUser = favUsers.any {
                    when (user) {
                        is UserItem -> it.username == user.login
                        is FavoriteUser -> it.username == user.username
                        else -> false
                    }
                }

                if (isFavUser) binding.ivFavorite.setImageResource(R.drawable.ic_favorite_filled)
                else binding.ivFavorite.setImageResource(R.drawable.ic_favorite_outline)
            } else binding.ivFavorite.setImageResource(R.drawable.ic_favorite_outline)

            Glide.with(binding.root)
                .load(when(user) {
                    is UserItem -> user.avatarUrl
                    is FavoriteUser -> user.avatarUrl
                    else -> false
                })
                .placeholder(R.drawable.account_circle)
                .into(binding.userImage)
        }
    }

}