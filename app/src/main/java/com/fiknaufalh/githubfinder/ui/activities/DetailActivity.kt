package com.fiknaufalh.githubfinder.ui.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.fiknaufalh.githubfinder.R
import com.fiknaufalh.githubfinder.adapters.SectionPagerAdapter
import com.fiknaufalh.githubfinder.data.response.UserDetailResponse
import com.fiknaufalh.githubfinder.database.FavoriteUser
import com.fiknaufalh.githubfinder.databinding.ActivityDetailBinding
import com.fiknaufalh.githubfinder.helpers.DateConverter
import com.fiknaufalh.githubfinder.helpers.Event
import com.fiknaufalh.githubfinder.helpers.ViewModelFactory
import com.fiknaufalh.githubfinder.viewmodels.DetailViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userName = intent.getStringExtra(resources.getString(R.string.passing_query))

        detailViewModel = getViewModel(this@DetailActivity)

        detailViewModel.userDetail.observe(this) {
            setUserDetail(it)
            setOnClickFab(it)
            binding.tvErrorDisplay.visibility = View.GONE

            val sectionPagerAdapter = SectionPagerAdapter(this, it.login!!)
            binding.viewPager.adapter = sectionPagerAdapter

            TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
                val currentTab = resources.getString(TAB_TITLES[position])
                val count = if (currentTab == resources.getString(R.string.tab_followers)) {
                    detailViewModel.userDetail.value?.followers
                } else {
                    detailViewModel.userDetail.value?.following
                }

                tab.text = resources.getString(R.string.tab_text, count, currentTab)
            }.attach()
            supportActionBar?.elevation = 0f

            if (detailViewModel.isFavorite(it.login)) {
                binding.fabFavorite?.setImageResource(R.drawable.ic_favorite_filled)
            } else {
                binding.fabFavorite?.setImageResource(R.drawable.ic_favorite_outline)
            }
        }

        detailViewModel.isLoading.observe(this) {
            loading -> showLoading(loading)
        }

        detailViewModel.errorMsg.observe(this) {
            msg -> setErrorMessage(msg)
        }

        detailViewModel.getDetail(userName!!)

        binding.backTab.setOnClickListener {
            finish()
        }
    }

    private fun getViewModel(activity: DetailActivity): DetailViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[DetailViewModel::class.java]
    }

    private fun setUserDetail(detail: UserDetailResponse) {
        val dateStr = detail.createdAt
        val convertedDate = DateConverter().formatDate(dateStr!!)

        with(binding) {
            tvUserName.text = detail.login
            tvFullName.text = if (detail.name.isNullOrEmpty()) resources.getString(R.string.undefinedName) else detail.name
            tvMemberSince.text = resources.getString(R.string.member_since, convertedDate)
            if (detail.email != null) {
                tvEmail.text = detail.email.toString()
            } else {
                tvEmail.visibility = View.GONE
            }
            ivUserProfile.borderColor = resources.getColor(R.color.white)
            ivUserProfile.borderWidth = 2

            ivUserProfile.setOnClickListener {
                val gitHubIntent = Intent(Intent.ACTION_VIEW, Uri.parse(detail.htmlUrl))
                startActivity(gitHubIntent)
            }
        }

        Glide.with(this)
            .load(detail.avatarUrl)
            .placeholder(R.drawable.account_circle)
            .into(binding.ivUserProfile)
    }

    private fun setOnClickFab(user: UserDetailResponse) {
        binding.fabFavorite?.setOnClickListener {
            val favoriteUser = FavoriteUser()
            favoriteUser.username = user.login!!
            favoriteUser.avatarUrl = user.avatarUrl
            favoriteUser.htmlUtl = user.htmlUrl!!

            if (detailViewModel.isFavorite(favoriteUser.username)) {
                detailViewModel.removeFavorite(favoriteUser)
                binding.fabFavorite?.setImageResource(R.drawable.ic_favorite_outline)

            } else {
                detailViewModel.addFavorite(favoriteUser)
                binding.fabFavorite?.setImageResource(R.drawable.ic_favorite_filled)
            }
        }
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun setErrorMessage(msg: Event<String>) {
        msg.getContentIfNotHandled()?.let {
            binding.tvErrorDisplay.visibility = View.VISIBLE
            binding.tvErrorDisplay.text = it
            val snackBar = Snackbar.make(
                window.decorView.rootView,
                it,
                Snackbar.LENGTH_SHORT
            )
            snackBar.anchorView = binding.botView
            snackBar.show()
        }
    }

    companion object {
        private val TAB_TITLES = intArrayOf(
            R.string.tab_followers,
            R.string.tab_following
        )
    }
}