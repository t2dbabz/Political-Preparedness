package com.example.android.politicalpreparedness.ui.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
//import com.example.android.politicalpreparedness.PoliticalPrepApplication
////import com.example.android.politicalpreparedness.data.source.remote.RemoteDataSourceImpl
//import com.example.android.politicalpreparedness.data.source.remote.network.CivicsApi
//import com.example.android.politicalpreparedness.data.source.repository.RepositoryImpl
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.ui.election.adapter.ElectionListAdapter

class ElectionsFragment: Fragment() {

    //TODO: Declare ViewModel
    private val viewModel by viewModels<ElectionsViewModel>()
//        val newRepo = RepositoryImpl(RemoteDataSourceImpl(CivicsApi.retrofitService))
//        ElectionsViewModelFactory(newRepo)


    private lateinit var binding: FragmentElectionBinding

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = FragmentElectionBinding.inflate(inflater)
        binding.lifecycleOwner = this




        //TODO: Add ViewModel values and create ViewModel

        //TODO: Add binding values

        //TODO: Link elections to voter info

        //TODO: Initiate recycler adapters

        //TODO: Populate recycler adapters

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ElectionListAdapter(ElectionListAdapter.ElectionListener { election ->
            findNavController().navigate(
                ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(
                    election.id,
                    election.division
                )
            )
        })

        binding.upcomingElectionsRecyclerView.adapter = adapter
        binding.upcomingElectionsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.election.observe(viewLifecycleOwner, { elections ->

            adapter.submitList(elections)

        })

    }

    //TODO: Refresh adapters when fragment loads

}