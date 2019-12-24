package com.orhan.comedchat_demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.cometchat.pro.constants.CometChatConstants
import com.cometchat.pro.core.CometChat
import com.cometchat.pro.exceptions.CometChatException
import com.cometchat.pro.models.TextMessage
import com.cometchat.pro.models.User

class MainActivity : AppCompatActivity() {
    private val TAG="MainActivity"
    var resId: Int = 0

    override fun onResume() {
        super.onResume()
        resId = R.id.main_frame
        val oneToOneFragment = OneToOneFragment().apply {
            arguments = Bundle().apply {
                //                    putString(StringContract.IntentString.USER_ID, t.uid)
//                    putString(StringContract.IntentString.USER_NAME, t.name)
//                    putString(StringContract.IntentString.USER_AVATAR, t.avatar)
//                    putString(StringContract.IntentString.USER_STATUS, t.status)
//                    putLong(StringContract.IntentString.LAST_ACTIVE, t.lastActiveAt)

            }
        }
        supportFragmentManager.beginTransaction()
            .replace(resId, oneToOneFragment).addToBackStack(null).commit()
//        send_btn.setOnClickListener {
//            //            val text = edt_msg.text
//            sendMessage(edt_msg.text.toString())
//
//        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

//        val edt_msg = findViewById(R.id.message) as EditText
//        val send_btn = findViewById(R.id.send) as Button



        onLoginClick("superhero10")
    }

    fun onLoginClick(uid:String){

        Log.d("Login", uid)

        kotlin.run {
            Toast.makeText(this@MainActivity,getString(R.string.please_wait), Toast.LENGTH_SHORT).show()
            CometChat.login(uid.trim(), StringContract.AppDetails.API_KEY, object : CometChat.CallbackListener<User>() {
                override fun onSuccess(p0: User?) {

                    Log.d(TAG,"login: ${StringContract.AppDetails.APP_ID}_user_${p0?.uid}")


                }

                override fun onError(p0: CometChatException?) {
                    Toast.makeText(this@MainActivity, p0?.message, Toast.LENGTH_SHORT).show()

                    Log.d(TAG,"onError:Login "+p0?.message)

                }

            })

        }


    }


    fun sendMessage(msg:String)
    {

        val receiverID:String="superhero3"
       // val messageText:String="Sending message from demo app"
        val messageType:String= CometChatConstants.MESSAGE_TYPE_TEXT
        val receiverType:String=CometChatConstants.RECEIVER_TYPE_USER

        val textMessage = TextMessage(receiverID, msg,receiverType)

        CometChat.sendMessage(textMessage, object : CometChat.CallbackListener<TextMessage>() {
            override fun onSuccess(p0: TextMessage?) {
                Log.d(TAG, "Message sent successfully: " + p0?.toString())
            }

            override fun onError(p0: CometChatException?) {
                Log.d(TAG, "Message sending failed with exception: " + p0?.message)          }

        })
    }
}
