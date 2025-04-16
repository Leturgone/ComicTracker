package com.example.comictracker.data.api.dto.charactersDTO

import com.google.gson.annotations.SerializedName


/**
 * Events
 *
 * @property available
 * @property returned
 * @property collectionURI
 * @property items
 * @constructor Create empty Events
 */
data class Events (

  @SerializedName("available"     ) var available     : String?          = null,
  @SerializedName("returned"      ) var returned      : String?          = null,
  @SerializedName("collectionURI" ) var collectionURI : String?          = null,
  @SerializedName("items"         ) var items         : ArrayList<Items> = arrayListOf()

)