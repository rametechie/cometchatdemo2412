package com.orhan.comedchat_demo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.collection.LongSparseArray
import androidx.recyclerview.widget.RecyclerView
import com.cometchat.pro.models.BaseMessage
import android.content.Context
import android.graphics.PorterDuff
import android.graphics.Typeface
import androidx.databinding.DataBindingUtil
import com.cometchat.pro.constants.CometChatConstants
import com.cometchat.pro.models.TextMessage
import com.inscripts.cometchatpulse.ViewHolder.LeftTextMessageHolder
import com.inscripts.cometchatpulse.ViewHolder.RightTextMessageHolder
import com.orhan.comedchat_demo.databinding.LeftTextBinding
import com.orhan.comedchat_demo.databinding.RightTextBinding

class OneToOneAdapter(val context: Context, val ownerId: String) :
    androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {
    private lateinit var viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder
    private var messagesList: LongSparseArray<BaseMessage> = LongSparseArray()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {

        val layoutInflater: LayoutInflater = LayoutInflater.from(context)

        when (p1) {

            StringContract.ViewType.RIGHT_TEXT_MESSAGE -> {
                val binding: RightTextBinding =
                    DataBindingUtil.inflate(layoutInflater, R.layout.right_text, p0, false)
                return RightTextMessageHolder(binding)
            }

            StringContract.ViewType.LEFT_TEXT_MESSAGE -> {
                val binding: LeftTextBinding =
                    DataBindingUtil.inflate(layoutInflater, R.layout.left_text, p0, false)
                return LeftTextMessageHolder(binding)
            }
            else -> {
                return viewHolder
            }
        }

    }


    override fun getItemCount(): Int {

        return messagesList.size()
    }

    override fun onBindViewHolder(p0: androidx.recyclerview.widget.RecyclerView.ViewHolder, p1: Int) {

        val baseMessage = messagesList.get(messagesList.keyAt(p0.adapterPosition))
        val timeStampLong = messagesList.get(messagesList.keyAt(p0.adapterPosition))?.sentAt
        var message: String? = null

        when (p0.itemViewType) {


            StringContract.ViewType.RIGHT_TEXT_MESSAGE -> {
                val rightTextMessageHolder = p0 as RightTextMessageHolder
                rightTextMessageHolder.binding.message = (baseMessage as TextMessage)
//                rightTextMessageHolder.binding.tvMessage.typeface = StringContract.Font.message
//                rightTextMessageHolder.binding.timestamp.typeface = StringContract.Font.status
                rightTextMessageHolder.binding.tvMessage.background.setColorFilter(StringContract.Color.rightMessageColor, PorterDuff.Mode.SRC_ATOP)
//                setLongClick(rightTextMessageHolder.binding.root, baseMessage)
//                setStatusIcon(rightTextMessageHolder.binding.imgMessageStatus,baseMessage)

                if (baseMessage.deletedAt!=0L){
                    rightTextMessageHolder.binding.tvMessage.text="message deleted"
                    rightTextMessageHolder.binding.tvMessage.setTypeface(null, Typeface.ITALIC)
                    rightTextMessageHolder.binding.tvMessage.setTextColor(context.resources.getColor(R.color.deletedTextColor))
                }
                else {
                    rightTextMessageHolder.binding.tvMessage.text = baseMessage.text
                    rightTextMessageHolder.binding.tvMessage.setTextColor(StringContract.Color.white)
//                    rightTextMessageHolder.binding.tvMessage.typeface = StringContract.Font.message
                }

            }

            StringContract.ViewType.LEFT_TEXT_MESSAGE -> {
                val leftTextMessageHolder = p0 as LeftTextMessageHolder
                leftTextMessageHolder.binding.message = (baseMessage as TextMessage)
//                leftTextMessageHolder.binding.senderName.typeface = StringContract.Font.status
//                leftTextMessageHolder.binding.timestamp.typeface = StringContract.Font.status
                leftTextMessageHolder.binding.tvMessage.background.setColorFilter(StringContract.Color.leftMessageColor,PorterDuff.Mode.SRC_ATOP)

              //  setLongClick(leftTextMessageHolder.binding.root, baseMessage)

                if (baseMessage.deletedAt!=0L){
                    leftTextMessageHolder.binding.tvMessage.text="message deleted"
                    leftTextMessageHolder.binding.tvMessage.setTextColor(context.resources.getColor(R.color.deletedTextColor))
                    leftTextMessageHolder.binding.tvMessage.setTypeface(null,Typeface.ITALIC)
                }
                else {
                    leftTextMessageHolder.binding.tvMessage.text = baseMessage.text
                    leftTextMessageHolder.binding.tvMessage.setTextColor(StringContract.Color.black)
//                    leftTextMessageHolder.binding.tvMessage.typeface = StringContract.Font.message
                }

            }





        }
    }

    fun setMessageList(messageList: MutableList<BaseMessage>) {
        for (baseMessage: BaseMessage in messageList) {
            this.messagesList.put(baseMessage.id.toLong(), baseMessage)
        }

        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {

        if (ownerId.equals(
                messagesList.get(messagesList.keyAt(position))?.sender?.uid,
                ignoreCase = true
            )
        ) {
            when (messagesList.get(messagesList.keyAt(position))?.type) {
                CometChatConstants.MESSAGE_TYPE_TEXT -> {

                    return StringContract.ViewType.RIGHT_TEXT_MESSAGE
                }

            }

        } else {
            when (messagesList.get(messagesList.keyAt(position))?.type) {

                CometChatConstants.MESSAGE_TYPE_TEXT -> {


                    return StringContract.ViewType.LEFT_TEXT_MESSAGE
                }
            }
        }
       return super.getItemViewType(position)
    }


}