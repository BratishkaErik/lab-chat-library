package io.github.bratishkaerik.chatlibrary

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

internal class MessageAdapter(private val messages: MutableList<Message> = mutableListOf()) :
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.message_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]

        holder.textView.apply {
            text = message.text
            background = GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                cornerRadius = 16f
                setStroke(2, Color.BLACK)
                setColor(Color.WHITE)
            }

            when (message.sender) {
                Message.Sender.SERVER -> {
                    gravity = Gravity.START
//                    textAlignment = View.TEXT_ALIGNMENT_TEXT_START
                }

                Message.Sender.CLIENT -> {
                    gravity = Gravity.END
//                    textAlignment = View.TEXT_ALIGNMENT_TEXT_END
                }
            }
        }
    }

    override fun getItemCount(): Int = messages.size

    fun addMessage(message: Message) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }
}

internal data class Message(val text: String, val sender: Sender) {
    enum class Sender {
        SERVER,
        CLIENT,
    }
}