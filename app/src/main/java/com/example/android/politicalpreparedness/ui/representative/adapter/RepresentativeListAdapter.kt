package com.example.android.politicalpreparedness.ui.representative.adapter

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.data.source.remote.network.models.Channel
import com.example.android.politicalpreparedness.databinding.RepresentativesListItemBinding
import com.example.android.politicalpreparedness.ui.representative.model.Representative

class RepresentativeListAdapter: ListAdapter<Representative, RepresentativeViewHolder>(RepresentativeDiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepresentativeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RepresentativesListItemBinding.inflate(inflater, parent, false)
        return RepresentativeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RepresentativeViewHolder, position: Int) {
        val representative = getItem(position)
        holder.bind(representative)
    }

}

class RepresentativeViewHolder(val binding: RepresentativesListItemBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(representative: Representative) {
        binding.representative = representative
        binding.representativeProfileImageView.setImageResource(R.drawable.ic_profile)

        representative.official.channels?.let {
            showSocialLinks(it)
        }

        representative.official.urls?.let {
            showWWWLinks(it)
        }



        //TODO: Show social links ** Hint: Use provided helper methods
        //TODO: Show www link ** Hint: Use provided helper methods

        binding.executePendingBindings()
    }

    //TODO: Add companion object to inflate ViewHolder (from)

    private fun showSocialLinks(channels: List<Channel>) {
        val facebookUrl = getFacebookUrl(channels)
        if (!facebookUrl.isNullOrBlank()) { enableLink(binding.representativeFacebookProfile, facebookUrl) }

        val twitterUrl = getTwitterUrl(channels)
        if (!twitterUrl.isNullOrBlank()) { enableLink(binding.representativeTwitterProfile, twitterUrl) }
    }

    private fun showWWWLinks(urls: List<String>) {
        enableLink(binding.representativeWebsite, urls.first())
    }

    private fun getFacebookUrl(channels: List<Channel>): String? {
        return channels.filter { channel -> channel.type == "Facebook" }
                .map { channel -> "https://www.facebook.com/${channel.id}" }
                .firstOrNull()
    }

    private fun getTwitterUrl(channels: List<Channel>): String? {
        return channels.filter { channel -> channel.type == "Twitter" }
                .map { channel -> "https://www.twitter.com/${channel.id}" }
                .firstOrNull()
    }

    private fun enableLink(view: ImageView, url: String) {
        view.visibility = View.VISIBLE
        view.setOnClickListener { setIntent(url) }
    }

    private fun setIntent(url: String) {
        val uri = Uri.parse(url)
        val intent = Intent(ACTION_VIEW, uri)
        itemView.context.startActivity(intent)
    }



}

class RepresentativeDiffCallback : DiffUtil.ItemCallback<Representative>() {
    override fun areItemsTheSame(oldItem: Representative, newItem: Representative): Boolean {
        return oldItem.office === newItem.office
    }

    override fun areContentsTheSame(oldItem: Representative, newItem: Representative): Boolean {
        return oldItem.official == newItem.official
    }
}

//TODO: Create RepresentativeDiffCallback

//TODO: Create RepresentativeListener