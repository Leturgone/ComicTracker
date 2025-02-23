package com.example.comictracker.data.api.dto.ComicsDTO

import com.google.gson.annotations.SerializedName


data class Images (

  @SerializedName("path"      ) var path      : String? = null,
  @SerializedName("extension" ) var extension : String? = null

)