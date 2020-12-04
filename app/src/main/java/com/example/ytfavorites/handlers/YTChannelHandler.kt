package com.example.ytfavorites.handlers

import com.example.ytfavorites.YTChannel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class YTChannelHandler{
    var db : FirebaseDatabase = FirebaseDatabase.getInstance()
    var channelRef : DatabaseReference

    init {
        channelRef = db.getReference("channels")
    }

    fun create(ytList: YTChannel): Boolean{
        val id = channelRef.push().key
        ytList.id = id

        channelRef.child(id!!).setValue(ytList)
        return true
    }

    fun delete(ytId: String){
        channelRef.child(ytId).removeValue()
    }

    fun edit(ytList: YTChannel): Boolean{
        channelRef.child(ytList.id!!).setValue(ytList)
        return true
    }
}