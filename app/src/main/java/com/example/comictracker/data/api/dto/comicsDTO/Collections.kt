package com.example.comictracker.data.api.dto.comicsDTO

import com.google.gson.annotations.SerializedName


data class Collections (

  @SerializedName("resourceURI" ) var resourceURI : String? = null,
  @SerializedName("name"        ) var name        : String? = null

)