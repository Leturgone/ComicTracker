package com.example.comictracker.data.api.dto

import com.google.gson.annotations.SerializedName


data class Prices (

  @SerializedName("type"  ) var type  : String? = null,
  @SerializedName("price" ) var price : String? = null

)