package com.example.trpg_try

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.trpg_try.databinding.ActivityChatBinding

class chat : AppCompatActivity() {
    private lateinit var chatbinding: ActivityChatBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var sendButton: Button
    private lateinit var messageEditText: EditText
    private lateinit var chatAdapter: ChatAdapter
    private val chatMessages = ArrayList<chatmessage>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        chatbinding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(chatbinding.root)

        recyclerView = findViewById(R.id.chat_recycler)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager


        chatAdapter = ChatAdapter(chatMessages)
        recyclerView.adapter = chatAdapter

        chatbinding.buttonSend.setOnClickListener{
            val message = chatbinding.editMes.text.toString()
            if (message.isNotEmpty()) {
                val chatMessage = chatmessage(message)
                chatMessages.add(chatMessage)
                Log.d("RecyclerView", "Data: " + chatMessages.toString())

                chatAdapter.notifyItemInserted(chatMessages.size - 1)

                chatbinding.editMes.text.clear()
        }

    }
}}