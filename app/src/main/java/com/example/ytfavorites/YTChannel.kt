package com.example.ytfavorites

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class YTChannel(var id: String? = " " , var title : String?= "", var link: String?="",
                var rank: Int = 0, var reason: String? = "") {
//    override fun toString(): String {
//        return "$rank   $title"
//    }
}