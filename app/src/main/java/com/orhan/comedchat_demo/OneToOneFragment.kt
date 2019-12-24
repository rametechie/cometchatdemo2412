package com.orhan.comedchat_demo

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.cometchat.pro.core.CometChat
import com.orhan.comedchat_demo.databinding.FragmentContactDetailBinding
import com.orhan.comedchat_demo.StringContract.ListenerName.Companion.MESSAGE_LISTENER
import kotlin.coroutines.CoroutineContext
import androidx.lifecycle.Observer
import com.cometchat.pro.constants.CometChatConstants
import com.cometchat.pro.models.TextMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONObject

class OneToOneFragment: Fragment(),View.OnClickListener {

    private lateinit var linearLayoutManager: androidx.recyclerview.widget.LinearLayoutManager

    lateinit var binding: FragmentContactDetailBinding

    private lateinit var onetoOneViewModel: OnetoOneViewModel

    private lateinit var oneToOneAdapter: OneToOneAdapter

    private var parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main

    private val scope = CoroutineScope(coroutineContext)

    private lateinit var userId: String

    private var ownerId: String

    init {
        ownerId = CometChat.getLoggedInUser().uid
        scrollFlag=true
    }

    companion object {

        var scrollFlag: Boolean = true
        var currentId: String? = null
        private val TAG = "OneToOneFragment"
    }

    override fun onStart() {
        super.onStart()
        currentId = userId
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_contact_detail, container, false)

        userId = "superhero11"



        onetoOneViewModel = OnetoOneViewModel(SaudiChatPro.applicationContext() as Application)




        linearLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)

        binding.recycler.layoutManager = linearLayoutManager

        oneToOneAdapter = OneToOneAdapter(context!!, CometChat.getLoggedInUser().uid)
        oneToOneAdapter.setHasStableIds(true)

        binding.recycler.adapter = oneToOneAdapter

        binding.rlMain.viewTreeObserver.addOnGlobalLayoutListener {
            val heightDiff = binding.rlMain.rootView.height - binding.rlMain.height
            try {

                if (heightDiff > CommonUtil.dpToPx(SaudiChatPro.applicationContext(), 200f)) {
                    binding.recycler.scrollToPosition(oneToOneAdapter.itemCount - 1)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        binding.cometchatToolbar.setBackgroundColor(StringContract.Color.primaryColor)

        binding.messageBox?.buttonSendMessage?.setOnClickListener(this)


        scope.launch(Dispatchers.IO) {
            onetoOneViewModel.fetchMessage(LIMIT = 30, userId = userId)

        }

        onetoOneViewModel.messageList.observe(this, Observer { messages ->
            messages?.let {
                oneToOneAdapter.setMessageList(it)
                if (scrollFlag) {
                    scrollBottom()
                    scrollFlag = false
                }

            }
        })

        binding.recycler.addOnScrollListener(object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: androidx.recyclerview.widget.RecyclerView, newState: Int) {

                binding.cometchatToolbar.isSelected = binding.recycler.canScrollVertically(-1)

                if (!recyclerView.canScrollVertically(-1)) {
                    onetoOneViewModel.fetchMessage(LIMIT = 30, userId = userId)
                    scrollFlag = false
                }
            }

            override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {

            }

        })

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ")
        currentId = userId
        onetoOneViewModel.receiveMessageListener(MESSAGE_LISTENER, ownerId)
        onetoOneViewModel.addPresenceListener(StringContract.ListenerName.USER_LISTENER)

   }
    private fun scrollBottom() {
        binding.recycler.scrollToPosition(oneToOneAdapter.itemCount - 1)
    }

    override fun onClick(p0: View?) {

        when (p0?.id) {


            R.id.buttonSendMessage -> {

                val messageText: String? = binding.messageBox?.editTextChatMessage?.text.toString().trim()

                if (messageText != null && !messageText.isEmpty()) {
                    val textMessage = TextMessage(userId, messageText, CometChatConstants.RECEIVER_TYPE_USER)

                    binding.messageBox?.editTextChatMessage?.setText("")

//                    if (!isEditMessage) {
//                        if (isReply) {
//                            binding.messageBox?.replyLayout?.rlMain?.visibility = View.GONE
//                            textMessage.metadata = metaData
//                            metaData = null
//                            metaData = JSONObject()
//                        }
                    onetoOneViewModel.sendTextMessage(textMessage)
//                    }
//                    else{
//                        isEditMessage=false
//                        binding.messageBox?.editTextChatMessage?.setText("")
//                        onetoOneViewModel.sendEditMessage(any as TextMessage,messageText)
//
//                    }


                    onetoOneViewModel.sendTypingIndicator(userId, true)

                }

                scrollFlag = true

            }


        }


    }

}