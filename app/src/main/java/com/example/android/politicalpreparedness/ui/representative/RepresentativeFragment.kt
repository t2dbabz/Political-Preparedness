package com.example.android.politicalpreparedness.ui.representative

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import com.example.android.politicalpreparedness.R
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.politicalpreparedness.BuildConfig
import com.example.android.politicalpreparedness.PoliticalPrepApplication
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.data.source.remote.network.models.Address
import com.example.android.politicalpreparedness.ui.representative.adapter.RepresentativeListAdapter
import com.example.android.politicalpreparedness.ui.representative.adapter.setNewValue
import com.example.android.politicalpreparedness.ui.representative.adapter.toTypedAdapter
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import java.util.Locale

class RepresentativeFragment : Fragment() {

    companion object {
        const val REQUEST_LOCATION_PERMISSION = 21
        const val MOTION_LAYOUT_STATE = "motion_layout_state"
    }

    private lateinit var binding: FragmentRepresentativeBinding

    private val viewModel: RepresentativeViewModel by viewModels {
        RepresentativeViewModelFactory((activity?.application as PoliticalPrepApplication).repository, this)
    }

    private val fusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentRepresentativeBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = RepresentativeListAdapter()

        binding.representativesRecyclerView.adapter = adapter

        binding.representativesRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        if (savedInstanceState != null) {
          val motionLayoutState =   savedInstanceState.getInt(MOTION_LAYOUT_STATE)
            binding.representativeMotionLayout.transitionToState(motionLayoutState)
            Log.i("RepFragment", "state restored")
        }



        viewModel.representatives.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })

        binding.findMyRepSearchButton.setOnClickListener {
            val address = Address(
                viewModel.addressLine1.value.toString(),
                viewModel.addressLine2.value.toString(),
                viewModel.cityInput.value.toString(),
                viewModel.stateInput.value.toString(),
                viewModel.zipInput.value.toString()
            )

            viewModel.getRepresentatives(address.toFormattedString())
            hideKeyboard()
        }


        binding.useLocationSearchButton.setOnClickListener {
            getLocationAndFindReps()
        }


        binding.state.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.i("RepFragment Spinner", position.toString())
                val selectedState = parent?.getItemAtPosition(position).toString()
                viewModel.onStateChanged(selectedState)
                Log.i("RepFragment Spinner", selectedState)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(MOTION_LAYOUT_STATE, binding.representativeMotionLayout.currentState)
        Log.i("RepFragment", "onSaveInstanceState Called")
    }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLocationAndFindReps()
            } else {
                requestPermissionWithRationaleIfNeeded()
            }
        }
    }

    private fun requestPermissionWithRationaleIfNeeded() {
        if (
            ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
        ) {
            AlertDialog.Builder(requireActivity())
                .setTitle(R.string.location_permission)
                .setMessage(R.string.permission_denied_explanation)
                .setPositiveButton("OK"){ _, _, ->
                    checkLocationPermissions()
                }
                .create()
                .show()

        } else {
            checkLocationPermissions()
        }
    }

    private fun checkLocationPermissions(): Boolean {
        return if (isPermissionGranted()) {
            true
        } else {
            requestPermissions(
                arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
            false
        }
    }

    private fun isPermissionGranted() : Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    private fun getLocationAndFindReps() {
        if(checkLocationPermissions()) {
            fusedLocationProviderClient.lastLocation
                .addOnSuccessListener { location : Location? ->
                    location?.let {
                        val address = geoCodeLocation(location)
                        viewModel.setAddress(address)
                        viewModel.getRepresentatives(address.toFormattedString())
                    }
                }

        } else {
            checkLocationPermissions()
        }
    }

    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
                .map { address ->

                    Address(address.thoroughfare, address.subThoroughfare, address.locality, address.adminArea, address.postalCode)
                }
                .first()
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
    }

}