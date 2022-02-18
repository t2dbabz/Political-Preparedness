package com.example.android.politicalpreparedness.ui.representative.model

import com.example.android.politicalpreparedness.data.source.remote.network.models.Office
import com.example.android.politicalpreparedness.data.source.remote.network.models.Official

data class Representative (
        val official: Official,
        val office: Office
)