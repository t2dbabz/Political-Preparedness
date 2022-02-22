package com.example.android.politicalpreparedness.ui.representative

import android.util.Log
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.data.source.remote.network.models.Address
import com.example.android.politicalpreparedness.data.source.repository.Repository
import com.example.android.politicalpreparedness.ui.representative.model.Representative
import com.example.android.politicalpreparedness.utils.Result
import kotlinx.coroutines.launch

class RepresentativeViewModel(private val repository: Repository, private val savedStateHandle: SavedStateHandle): ViewModel() {


    companion object {
        const val ADDRESS_LINE_1 = "address_line_1"
        const val ADDRESS_LINE_2 = "address_line_2"
        const val CITY_INPUT = "city_input"
        const val ZIP_INPUT = "zip_input"
        const val STATE_INPUT = "state_input"
    }

    private val _representatives = MutableLiveData<List<Representative>>()
    val representatives: LiveData<List<Representative>> = _representatives

    val addressLine1: MutableLiveData<String> = savedStateHandle.getLiveData(ADDRESS_LINE_1)
    val addressLine2: MutableLiveData<String> = savedStateHandle.getLiveData(ADDRESS_LINE_2)
    val cityInput: MutableLiveData<String>    = savedStateHandle.getLiveData(CITY_INPUT)
    val zipInput: MutableLiveData<String> = savedStateHandle.getLiveData(ZIP_INPUT)
    val stateInput: MutableLiveData<String> = savedStateHandle.getLiveData(STATE_INPUT)


    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    /**
     *  The following code will prove helpful in constructing a representative from the API. This code combines the two nodes of the RepresentativeResponse into a single official :

    val (offices, officials) = getRepresentativesDeferred.await()
    _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }

    Note: getRepresentatives in the above code represents the method used to fetch data from the API
    Note: _representatives in the above code represents the established mutable live data housing representatives

     */


    fun getRepresentatives(address: String) {
        viewModelScope.launch {
            _isLoading.value =true
            try {
                when(val result = repository.getRepresentatives(address)) {

                    is Result.Success -> {
                        _isLoading.value = false
                        if (result.data != null) {
                            val (offices, officials) = result.data
                            Log.d("RepViewModel", result.data.offices.lastIndex.toString())
                            _representatives.value = offices.flatMap { office ->
                                office.getRepresentatives(officials)
                            }
                        }
                    }

                    is Result.Error -> {
                        _isLoading.value = false

                    }

                    is Result.Loading -> {
                        _isLoading.value = true
                    }
                }
            } catch (e : Exception) {

            }
        }
    }



    fun setAddress(currentAddress: Address) {

        addressLine1.value = currentAddress.line1
        addressLine2.value = currentAddress.line2 ?: ""
        cityInput.value = currentAddress.city
        stateInput.value = currentAddress.state
        zipInput.value = currentAddress.zip

    }

    fun onLine1Changed(s: CharSequence, start: Int, before: Int, count: Int) {
        // save the corresponding value to SavedStateHandle
        savedStateHandle[ADDRESS_LINE_1] = s.toString()
    }

    fun onLine2Changed(s: CharSequence, start: Int, before: Int, count: Int) {
        // save the corresponding value to SavedStateHandle
        savedStateHandle[ADDRESS_LINE_2] = s.toString()
    }

    fun onCityChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        // save the corresponding value to SavedStateHandle
        savedStateHandle[CITY_INPUT] = s.toString()
    }

    fun onZipChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        // save the corresponding value to SavedStateHandle
        savedStateHandle[ZIP_INPUT] = s.toString()
    }

    fun onStateChanged(state: String) {
        stateInput.value = state
        savedStateHandle[STATE_INPUT] = state
    }

}
