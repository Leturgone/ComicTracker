package com.example.comictracker.data.api.dto.creatorsDTO

import com.google.gson.annotations.SerializedName


data class CreatorsDTO (

    @SerializedName("code"            ) var code            : String? = null,
    @SerializedName("status"          ) var status          : String? = null,
    @SerializedName("copyright"       ) var copyright       : String? = null,
    @SerializedName("attributionText" ) var attributionText : String? = null,
    @SerializedName("attributionHTML" ) var attributionHTML : String? = null,
    @SerializedName("data"            ) var data            : Data?   = Data(),
    @SerializedName("etag"            ) var etag            : String? = null

)