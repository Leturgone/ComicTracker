package com.example.comictracker.data.api

import java.math.BigInteger
import java.security.MessageDigest
import java.sql.Timestamp



val timeStamp = Timestamp(System.currentTimeMillis()).toString()



fun hash(): String{
    val input = "$timeStamp$PRIVATE_KEY$API_KEY"
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1,md.digest(input.toByteArray())).toString(16).padStart(32,'0')
}