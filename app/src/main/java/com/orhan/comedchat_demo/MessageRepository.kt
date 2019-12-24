package com.orhan.comedchat_demo

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import android.content.Context
import androidx.annotation.WorkerThread
import android.util.Log
import android.widget.RelativeLayout
import android.widget.Toast
import com.cometchat.pro.constants.CometChatConstants
import com.cometchat.pro.core.Call
import com.cometchat.pro.core.CometChat
import com.cometchat.pro.core.MessagesRequest
import com.cometchat.pro.exceptions.CometChatException
import com.cometchat.pro.models.*
//import com.inscripts.cometchatpulse.Activities.CallActivity
//import com.inscripts.cometchatpulse.Activities.LocationActivity
//import com.inscripts.cometchatpulse.CometChatPro
//import com.inscripts.cometchatpulse.Fragment.OneToOneFragment
//import com.inscripts.cometchatpulse.Utils.CommonUtil
import com.cometchat.pro.models.MessageReceipt
import com.cometchat.pro.models.MediaMessage
import com.cometchat.pro.models.TextMessage
import com.cometchat.pro.models.BaseMessage
//import com.inscripts.cometchatpulse.Fragment.GroupFragment

class MessageRepository {

    var ownerId: String

    var messageRequest: MessagesRequest? = null

    var groupMessageRequest: MessagesRequest? = null

    var user: MutableLiveData<User> = MutableLiveData()

    init {
        ownerId = CometChat.getLoggedInUser().uid
    }

    var onetoOneMessageList: MutableLiveData<MutableList<BaseMessage>> = MutableLiveData()

    var filterLiveonetoOneMessageList: MutableLiveData<MutableList<BaseMessage>> = MutableLiveData()

    var groupMessageList: MutableLiveData<MutableList<BaseMessage>> = MutableLiveData()

    var filterLivegroupMessageList: MutableLiveData<MutableList<BaseMessage>> = MutableLiveData()

    var mutableOneToOneMessageList = mutableListOf<BaseMessage>()

    var filterMutableOneToOneMessageList = mutableListOf<BaseMessage>()

    var mutableGroupMessageList = mutableListOf<BaseMessage>()

    var filterGroupMessageList = mutableListOf<BaseMessage>()

    var liveStartTypingIndicator: MutableLiveData<TypingIndicator> = MutableLiveData()

    var liveEndTypingIndicator: MutableLiveData<TypingIndicator> = MutableLiveData()

    var liveReadReceipts: MutableLiveData<MessageReceipt> = MutableLiveData()

    var liveDeliveryReceipts: MutableLiveData<MessageReceipt> = MutableLiveData()

    var liveMessageEdited: MutableLiveData<BaseMessage> = MutableLiveData()

    var liveMessageDeleted: MutableLiveData<BaseMessage> = MutableLiveData()

    private val TAG = "MessageRepository"

    @WorkerThread
    fun fetchMessage(LIMIT: Int, userId: String,isRefresh:Boolean) {

        try {
              if (isRefresh){
                  messageRequest= null
              }

            if (messageRequest == null) {
                messageRequest = MessagesRequest.MessagesRequestBuilder().setUID(userId).setLimit(LIMIT).build()
            }
            fetchMessageRequest(messageRequest)
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

    }

    private fun fetchMessageRequest(messagesRequest: MessagesRequest?){

        messagesRequest?.fetchPrevious(object : CometChat.CallbackListener<List<BaseMessage>>() {
            override fun onSuccess(p0: List<BaseMessage>?) {

                if (p0 != null) {
                    for (baseMessage: BaseMessage in p0) {
                        Log.d(TAG, "baseMessage onSuccess: " + baseMessage.id)
                        if (baseMessage.category != CometChatConstants.CATEGORY_ACTION && baseMessage.deletedAt == 0L) {
                            mutableOneToOneMessageList.add(baseMessage)
                        }
                    }
                    if(p0.isNotEmpty()){
                        val message=p0.get(p0.size-1)
                        CometChat.markAsRead(message.id,message.sender.uid,message.receiverType);
                    }
                    onetoOneMessageList.value = mutableOneToOneMessageList
                }

                Log.d(TAG, "messageRequest onSuccess: ${p0?.size}")

            }

            override fun onError(p0: CometChatException?) {
                Toast.makeText(SaudiChatPro.applicationContext(), p0?.message, Toast.LENGTH_SHORT).show()
                Log.d(TAG, "messageRequest onError: ${p0?.message}")
            }

        })
    }

    @WorkerThread
    fun sendTextMessage(textMessage: TextMessage, context: Context? = null) {

        CometChat.sendMessage(textMessage, object : CometChat.CallbackListener<TextMessage>() {
            override fun onSuccess(p0: TextMessage?) {
                if (p0 != null) {
                    Toast.makeText(SaudiChatPro.applicationContext(), "Text Message Sent Successfully", Toast.LENGTH_SHORT).show()
                    Log.d("messageDao", "  $p0")
                    if (p0.receiverType.equals(CometChatConstants.RECEIVER_TYPE_USER)) {
                        mutableOneToOneMessageList.add(p0)
                        onetoOneMessageList.value = mutableOneToOneMessageList

                    } else {
                        mutableGroupMessageList.add(p0)
                        groupMessageList.value = mutableGroupMessageList

                    }

//                    if (context != null) {
//                        if (context is LocationActivity)
//                            context.finish()
//                    }
                }

            }

            override fun onError(p0: CometChatException?) {
                Toast.makeText(SaudiChatPro.applicationContext(), p0?.message, Toast.LENGTH_SHORT).show()

            }

        })
    }


    @WorkerThread
    fun messageReceiveListener(listener: String) {

        CometChat.addMessageListener(listener, object : CometChat.MessageListener() {

            override fun onMessagesDelivered(messageReceipt: MessageReceipt?) {
                if (messageReceipt != null&&messageReceipt.receiverId== CometChat.getLoggedInUser().uid )
                    liveDeliveryReceipts.value = messageReceipt
            }

            override fun onMessagesRead(messageReceipt: MessageReceipt?) {
                if (messageReceipt != null&&messageReceipt.receiverId== CometChat.getLoggedInUser().uid)
                    liveReadReceipts.value = messageReceipt
            }

            override fun onMessageEdited(message: BaseMessage?) {
                Log.d(TAG, "onMessageEdited: $message")
                liveMessageEdited.value = message
            }

            override fun onMessageDeleted(message: BaseMessage?) {
                liveMessageDeleted.value = message
            }



            override fun onTypingEnded(typingIndicator: TypingIndicator?) {
                if (typingIndicator != null)
                    liveEndTypingIndicator.value = typingIndicator

            }

            override fun onTypingStarted(typingIndicator: TypingIndicator?) {
                if (typingIndicator != null)
                    liveStartTypingIndicator.value = typingIndicator

            }

            override fun onTextMessageReceived(p0: TextMessage?) {
                if (p0 != null) {


                    Log.d(TAG, "onTextMessageReceived:   $p0")

                    if (!p0.receiverType.equals(CometChatConstants.RECEIVER_TYPE_GROUP)) {
                        try {
                               //OneToOneFragment.scrollFlag = true
                                mutableOneToOneMessageList.add(p0)
                                onetoOneMessageList.value = mutableOneToOneMessageList
                                CometChat.markAsRead(p0.id,p0.sender.uid,p0.receiverType)
                        } catch (e: NullPointerException) {
                            e.printStackTrace()
                        }
                    } else {
//                        CometChat.markAsRead(p0.id,p0.receiverUid,p0.receiverType)
//                        GroupFragment.scrollFlag = true
//                        mutableGroupMessageList.add(p0)
//                        groupMessageList.value = mutableGroupMessageList

                    }
                }
            }

            override fun onMediaMessageReceived(p0: MediaMessage?) {
                if (p0 != null) {

                    Log.d(TAG, "onMediaMessageReceived: ${p0}")
                    if (!p0.receiverType.equals(CometChatConstants.RECEIVER_TYPE_GROUP)) {

                      //  OneToOneFragment.scrollFlag = true
                        mutableOneToOneMessageList.add(p0)
                        onetoOneMessageList.value = mutableOneToOneMessageList
                        CometChat.markAsRead(p0.id,p0.sender.uid,p0.receiverType)

                    } else {
                        CometChat.markAsRead(p0.id,p0.receiverUid,p0.receiverType)
                     //   GroupFragment.scrollFlag = true
                        mutableGroupMessageList.add(p0)
                        groupMessageList.value = mutableGroupMessageList
                    }
                }
            }

        })
    }

    @WorkerThread
    fun removeMessageListener(listener: String) {
        CometChat.removeMessageListener(listener)
    }

    @WorkerThread
    fun addPresenceListener(listener: String) {

        CometChat.addUserListener(listener, object : CometChat.UserListener() {
            override fun onUserOffline(p0: User?) {

                if (p0 != null) {
                    user.value = p0
                }
            }

            override fun onUserOnline(p0: User?) {
                if (p0 != null) {
                    user.value = p0
                }
            }

        })


    }


    @WorkerThread
    fun removePresenceListener(listener: String) {
        CometChat.removeUserListener(listener)
    }


    fun sendTypingIndicator(typingIndicator: TypingIndicator, isEndTyping: Boolean) {

        if (isEndTyping)
            CometChat.endTyping(typingIndicator)
        else
            CometChat.startTyping(typingIndicator)

    }

    fun deleteMessage(textMessage: TextMessage) {

        CometChat.deleteMessage(textMessage.id, object : CometChat.CallbackListener<BaseMessage>() {
            override fun onSuccess(p0: BaseMessage?) {
                if (p0 != null) {
                    if (p0.receiverType.equals(CometChatConstants.RECEIVER_TYPE_USER)) {
                        mutableOneToOneMessageList.add(p0)
                        onetoOneMessageList.value = mutableOneToOneMessageList
                    } else {
                        mutableGroupMessageList.add(p0)
                        groupMessageList.value = mutableGroupMessageList
                    }
                    Toast.makeText(SaudiChatPro.applicationContext(), "Delete Message Successfully", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onError(p0: CometChatException?) {
                Toast.makeText(SaudiChatPro.applicationContext(), p0?.message, Toast.LENGTH_SHORT).show()
                Log.d(TAG, "deleteMessage onError: ${p0?.message}")
            }

        })
    }

    fun editMessage(any: BaseMessage, messageText: String) {

        val textMessage = TextMessage(any.receiverUid, messageText, CometChatConstants.RECEIVER_TYPE_USER)

        textMessage.id = any.id

        Log.d(TAG, "editMessage: " + textMessage.id)

        CometChat.editMessage(textMessage, object : CometChat.CallbackListener<BaseMessage>() {

            override fun onSuccess(p0: BaseMessage?) {
                if (p0 != null) {
                    Toast.makeText(SaudiChatPro.applicationContext(), "Message Edited Successfully", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "editMessage: onSuccess " + p0.id)
                    if (p0.receiverType.equals(CometChatConstants.RECEIVER_TYPE_USER)) {
                        mutableOneToOneMessageList.add(p0)
                        onetoOneMessageList.value = mutableOneToOneMessageList
                    } else {
                        mutableGroupMessageList.add(p0)
                        groupMessageList.value = mutableGroupMessageList
                    }
                }
            }

            override fun onError(p0: CometChatException?) {
                Toast.makeText(SaudiChatPro.applicationContext(), p0?.message, Toast.LENGTH_SHORT).show()
                Log.d(TAG, "editMessage onError: ${p0?.message}")
            }

        })
    }


}