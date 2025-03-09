package com.example.comictracker.data.api.dto.comicsDTO

import com.google.gson.annotations.SerializedName


data class Images (

  @SerializedName("path"      ) var path      : String? = null,
  @SerializedName("extension" ) var extension : String? = null

)