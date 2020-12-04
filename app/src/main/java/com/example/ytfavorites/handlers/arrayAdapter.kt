package com.example.ytfavorites.handlers

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.ytfavorites.R
import com.example.ytfavorites.YTChannel

class arrayAdapter(var mcont: Context, var res: Int, var list: List<YTChannel>): ArrayAdapter<YTChannel>(mcont,res, list ){
    override fun getCount(): Int {
        return 10
    }
    override fun getItem(position: Int) : YTChannel? {
        return list[position]
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(mcont)
        val view = inflater.inflate(res, null)
        val title = view.findViewById<TextView>(R.id.titleDisplay)
        val rank = view.findViewById<TextView>(R.id.rankDisplay)
        title.text =list[position].title
        rank.text = list[position].rank.toString()
        return view
    }
}