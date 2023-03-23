package com.example.trpg_try

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trpg_try.character_list.Character

class char_adapter(val items: ArrayList<Character>) :
    RecyclerView.Adapter<char_adapter.HaruHolder>() {
    inner class HaruHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val test1= itemView.findViewById<TextView>(R.id.myname)
    }
    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HaruHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.main_list, parent, false)
        return HaruHolder(view)
    }
    override fun onBindViewHolder(holder: HaruHolder, position: Int) {
        holder.test1.text = items[position].charname
    }

}