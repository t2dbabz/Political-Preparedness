package com.example.android.politicalpreparedness.ui.election

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding

class VoterInfoFragment : Fragment() {

    private val viewModel by viewModels<VoterInfoViewModel>()


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {




        //TODO: Add ViewModel values and create ViewModel
        val binding = FragmentVoterInfoBinding.inflate(inflater)
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


    }



    //TODO: Create method to load URL intents

}