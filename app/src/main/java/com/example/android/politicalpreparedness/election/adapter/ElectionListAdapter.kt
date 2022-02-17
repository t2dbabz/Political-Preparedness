package com.example.android.politicalpreparedness.election.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.databinding.ElectionInfoListItemBinding
import com.example.android.politicalpreparedness.network.models.Election



class ElectionListAdapter(private val clickListener: ElectionListener): ListAdapter<Election,
        ElectionListAdapter.ElectionViewHolder>(ElectionDiffCallback) {


    class ElectionViewHolder(val binding: ElectionInfoListItemBinding ): RecyclerView.ViewHolder(binding.root){

        fun bind(election: Election) {
            binding.election = election
            binding.executePendingBindings()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElectionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ElectionInfoListItemBinding.inflate(inflater, parent, false)
        return ElectionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ElectionViewHolder, position: Int) {
        val election = getItem(position)
        holder.bind(election)
        holder.itemView.setOnClickListener {
            clickListener.onClick(election)
        }

    }

    //TODO: Bind ViewHolder

    //TODO: Add companion object to inflate ViewHolder (from)

    companion object ElectionDiffCallback : DiffUtil.ItemCallback<Election>() {
        override fun areItemsTheSame(oldItem: Election, newItem: Election): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Election, newItem: Election): Boolean {
            return oldItem.id == newItem.id
        }
    }

    class ElectionListener(val clickListener: (election: Election) -> Unit) {
        fun onClick(election: Election) = clickListener(election)
    }

}

//TODO: Create ElectionViewHolder

//TODO: Create ElectionDiffCallback

//TODO: Create ElectionListener

