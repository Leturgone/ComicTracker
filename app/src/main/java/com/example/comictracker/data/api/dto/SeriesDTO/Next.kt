package com.example.comictracker.data.api.dto.SeriesDTO

import com.google.gson.annotations.SerializedName


data class Next (

  @SerializedName("resourceURI" ) var resourceURI : String? = null,
  @SerializedName("name"        ) var name        : String? = null

)