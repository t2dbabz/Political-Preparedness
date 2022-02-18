package com.example.android.politicalpreparedness.data.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.android.politicalpreparedness.data.source.remote.network.models.Division
import com.squareup.moshi.Json
import java.util.*

@Entity(tableName = "saved_election_table")
data class SavedElectionInfo(
    @PrimaryKey
    val id: Int,

    val name: String,

    val electionDay: Date,

    @Embedded(prefix = "division_") @Json(name="ocdDivisionId") val division: Division,

    @ColumnInfo(name = "electionInfoUrl")
    val electionInfoUrl: String? = null,

    @ColumnInfo(name = "votingLocationUrl")
    val votingLocationFinderUrl: String? = null,

    @ColumnInfo(name = "ballotInfoUrl")
    val ballotInfoUrl: String? = null,

    )
