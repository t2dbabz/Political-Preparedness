package com.example.android.politicalpreparedness.ui.representative

import android.util.Log
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.data.source.remote.network.models.Address
import com.example.android.politicalpreparedness.data.source.repository.Repository
import com.example.android.politicalpreparedness.ui.representative.model.Representative
import com.example.android.politicalpreparedness.utils.Result
import kotlinx.coroutines.launch

class RepresentativeViewModel(private val repository: Repository, private val savedStateHandle: SavedStateHandle): ViewModel() {




    private val _representatives = MutableLiveData<List<Representative>>()
    val representatives: LiveData<List<Representative>> = _representatives

    val address = MutableLiveData(Address("", "", "", "", ""))

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    private val savedStateAddressObserver = Observer<Address> {
        // Most recently added this per Mentor Diraj
        address.value = it
        if (it != null) {
            getRepresentatives(it.toFormattedString())
        }

    }

    init {
        savedStateHandle.getLiveData<Address>("address").observeForever(savedStateAddressObserver)
    }


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
        address.value = currentAddress
        savedStateHandle["address"] = currentAddress

        Log.d("RepViewModel", address.value.toString())

    }

    override fun onCleared() {
        super.onCleared()
        savedStateHandle.getLiveData<Address>("address").removeObserver(savedStateAddressObserver)
    }


}
