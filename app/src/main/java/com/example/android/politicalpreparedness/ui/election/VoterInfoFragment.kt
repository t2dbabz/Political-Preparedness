package com.example.android.politicalpreparedness.ui.election

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.android.politicalpreparedness.PoliticalPrepApplication
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding

class VoterInfoFragment : Fragment() {

    private val viewModel by viewModels<VoterInfoViewModel>() {
        VoterInfoViewModelFactory((activity?.application as PoliticalPrepApplication).repository)
    }

    private lateinit var binding: FragmentVoterInfoBinding


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = FragmentVoterInfoBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel


        /**
        Hint: You will need to ensure proper data is provided from previous fragment.
        */

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = VoterInfoFragmentArgs.fromBundle(requireArguments())
        val electionId = bundle.argElectionId
        val division = bundle.argDivision

        if(division.state.isBlank()) {
            val address = "state:ca"
            println(address)
            println(electionId)
            Log.d("VoterFragment", address)
            viewModel.getVoterInfo(address, electionId)
        } else {
            val address = division.state
            println(address)
            println(electionId)
            Log.d("VoterFragment", address)
            viewModel.getVoterInfo(address, electionId)
        }

        viewModel.checkIfElectionFollowed(electionId.toInt())


        viewModel.isElectionFollowed.observe(viewLifecycleOwner,{ isFollowed ->
            if (isFollowed == true){
                binding.followElectionButton.text = getString(R.string.unfollow_election)
            } else {
                binding.followElectionButton.text = getString(R.string.follow_election)
            }
        })

        viewModel.electionInfoUrl.observe(viewLifecycleOwner, { url ->

            url?.let {
                loadUrl(url)
            }
        })

        viewModel.votingLocationFinderUrl.observe(viewLifecycleOwner, { url ->

            url?.let {
                loadUrl(url)
            }
        })

        viewModel.ballotInfoUrl.observe(viewLifecycleOwner, { url ->

            url?.let {
                loadUrl(url)
            }

        })
    }


    private fun loadUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

}