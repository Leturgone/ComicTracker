package com.example.comictracker.data.api.dto.comicsDTO

import com.google.gson.annotations.SerializedName


data class Dates (

  @SerializedName("type" ) var type : String? = null,
  @SerializedName("date" ) var date : String? = null

)