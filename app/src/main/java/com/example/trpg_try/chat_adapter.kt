package com.example.trpg_try

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChatAdapter(private val chatMessages: ArrayList<chatmessage>) : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // 아이템 뷰의 요소들을 참조하는 변수들을 정의합니다.
        val messageTextView: TextView = itemView.findViewById(R.id.chat_mbubble)
        //val senderTextView: TextView = itemView.findViewById(R.id.)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // 아이템 뷰를 생성하고 ViewHolder 객체를 반환합니다.
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.chat_bubble, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // 데이터를 아이템 뷰에 바인딩합니다.
        val chatMessage = chatMessages[position]
        holder.messageTextView.text = chatMessage.message
    }

    override fun getItemCount(): Int {
        // 데이터의 개수를 반환합니다.
        return chatMessages.size
    }
}
