package com.example.android.politicalpreparedness.ui.election

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.android.politicalpreparedness.PoliticalPrepApplication
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding

class VoterInfoFragment : Fragment() {

    private val viewModel by viewModels<VoterInfoViewModel>() {
        VoterInfoViewModelFactory((activity?.application as PoliticalPrepApplication).repository)
    }

    private lateinit var binding: FragmentVoterInfoBinding


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {



        //TODO: Add ViewModel values and create ViewModel
        binding = FragmentVoterInfoBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel


        //TODO: Add binding values

        //TODO: Populate voter info -- hide views without provided data.
        /**
        Hint: You will need to ensure proper data is provided from previous fragment.
        */


        //TODO: Handle loading of URLs

        //TODO: Handle save button UI state
        //TODO: cont'd Handle save button clicks
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
                binding.followElectionButton.text = "unfollow election"
            } else {
                binding.followElectionButton.text = "follow election"
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



    //TODO: Create method to load URL intents

}