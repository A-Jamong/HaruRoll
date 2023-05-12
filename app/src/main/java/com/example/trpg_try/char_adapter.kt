package com.example.trpg_try

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trpg_try.character_list.Character

class Char_adapter(val items: ArrayList<Character>) :
    RecyclerView.Adapter<Char_adapter.HaruHolder>() {
    inner class HaruHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val nameTextview: TextView= itemView.findViewById(R.id.nametest)
    }
    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HaruHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.main_list, parent, false)
        return HaruHolder(view)
    }
    override fun onBindViewHolder(holder: HaruHolder, position: Int) {
        holder.nameTextview.text = items[position].charname
    }

}