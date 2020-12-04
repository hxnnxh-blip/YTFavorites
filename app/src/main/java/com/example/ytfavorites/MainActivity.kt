package com.example.ytfavorites

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.ytfavorites.handlers.YTChannelHandler
import com.example.ytfavorites.handlers.arrayAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.lang.Exception


class MainActivity : AppCompatActivity() {

    lateinit var adapter : arrayAdapter
    lateinit var channelHandler: YTChannelHandler
    lateinit var titleET: EditText
    lateinit var linkET: EditText
    lateinit var rankET: EditText
    lateinit var reasonET: EditText
    lateinit var doneButton: Button
    lateinit var channelListView: ListView
    var ytChannels = arrayListOf<YTChannel>()
    var ytID = " "

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title = "YouTube Favorites"

        titleET = findViewById(R.id.channelTitle)
        linkET = findViewById(R.id.channelLink)
        rankET = findViewById(R.id.channelRanking)
        reasonET = findViewById(R.id.reasonForLiking)
        doneButton = findViewById(R.id.doneButton)
        channelHandler = YTChannelHandler()

        channelListView = findViewById(R.id.channelList)

        doneButton.setOnClickListener{choose()}
        registerForContextMenu(channelListView)

        channelListView.onItemClickListener =
                AdapterView.OnItemClickListener { _, _, position, _ ->
                    val inflater = this.layoutInflater
                    val view = inflater.inflate(R.layout.show_info,null)
                    val ytSelected = ytChannels[position]
                    val ytlink = ytSelected.link
                    val ytReason = ytSelected.reason
                    val resTxt = view.findViewById<TextView>(R.id.reasonDisplay)
                    val linkTxt = view.findViewById<TextView>(R.id.linkDisplay)
                    linkTxt.text = ytlink
                    resTxt.text = ytReason
                    val dialogBuilder = AlertDialog.Builder(this)
                            .setView(view)
                            .setNegativeButton("OK", DialogInterface.OnClickListener { _, _ ->
                            })
                    dialogBuilder.create()
                    dialogBuilder.show()
                }
    }

    override fun onStart() {
        super.onStart()
        channelHandler.channelRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                ytChannels.clear()
                snapshot.children.forEach{
                    it -> val channel = it.getValue(YTChannel::class.java)
                    ytChannels.add(channel!!)
                    ytChannels.sortBy { it.rank }
                }
                adapter = arrayAdapter(applicationContext, R.layout.listview_layout,ytChannels)
//                val adapter = ArrayAdapter(applicationContext, android.R.layout.simple_expandable_list_item_1,ytChannels)
                channelListView.adapter = adapter
            }
            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    fun choose(){
        val title = titleET.text.toString()
        val link = linkET.text.toString()
        val rank = rankET.text.toString().toInt()
        val reason = reasonET.text.toString()

        if (doneButton.text.toString() == "Add"){
            try {
                val channel = YTChannel(title = title, link = link,rank = rank,reason = reason)
                if (channelHandler.create(channel)) {
                    Toast.makeText(this,"Channel Added", Toast.LENGTH_SHORT).show()
                    clearFields()
                }
            }catch (e: Exception){
                Toast.makeText(this,"Failed to add channel", Toast.LENGTH_LONG).show()
            }
        }

        else if (doneButton.text.toString() == "Edit"){
            try {
                val channel = YTChannel(id = ytID, title = title, link = link,rank = rank,reason = reason)
                if (channelHandler.edit(channel)) {
                    Toast.makeText(this, "Channel Updated", Toast.LENGTH_LONG).show()
                    clearFields()
                }
            }catch (e: Exception){
                Toast.makeText(this, "Failed to update channel", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater
        inflater.inflate(R.menu.channel_options, menu)
    }

    @SuppressLint("SetTextI18n")
    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        val yt = ytChannels[info.position]
        val ytID = yt.id.toString()
        return when(item.itemId){
            R.id.deleteChannel ->{
                channelHandler.delete(ytID)
                return true
            }

            R.id.editChannel -> {
                val ychannel = ytChannels[info.position]
                titleET.setText(ychannel.title)
                linkET.setText(ychannel.link)
                rankET.setText(ychannel.rank.toString())
                reasonET.setText(ychannel.reason)
                doneButton.text = "Edit"

                doneButton.setOnClickListener{ choose() }
                return true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun clearFields(){
        titleET.text.clear()
        linkET.text.clear()
        rankET.text.clear()
        reasonET.text.clear()
        doneButton.text = "Add"
    }
}