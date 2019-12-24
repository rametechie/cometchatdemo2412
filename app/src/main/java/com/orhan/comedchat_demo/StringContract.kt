package com.orhan.comedchat_demo

import android.graphics.Typeface

class StringContract {

    class AppDetails {

        companion object {

            const val APP_ID:String = "12252035c505643"

            const val API_KEY:String = "62945e6875e6a12cfb2a2f7be4385176a9d98c9d"

            const val REGION:String = "us"



        }
    }

    class ViewType {

        companion object {

            const val RIGHT_TEXT_MESSAGE = 334

            const val LEFT_TEXT_MESSAGE = 734

            const val RIGHT_TEXT_REPLY_MESSAGE=346

            const val LEFT_MEDIA_REPLY_MESSAGE =756
        }
    }

    class Font {


        companion object {

//            lateinit var title: Typeface
//
//            lateinit var name: Typeface
//
//            lateinit var status: Typeface
//
//            lateinit var message: Typeface

        }

    }

    class Color {

        companion object {

            var primaryColor: Int = 0

            var primaryDarkColor: Int = 0

            var accentColor: Int = 0

            var rightMessageColor = 0

            var leftMessageColor = 0

            var iconTint = 0

            var white: Int = android.graphics.Color.parseColor("#ffffff")

            var black: Int = android.graphics.Color.parseColor("#000000")

            var grey: Int = android.graphics.Color.parseColor("#CACACC")

            var inactiveColor = android.graphics.Color.parseColor("#9e9e9e");

        }

    }

    class Dimensions {

        companion object {

            var marginStart: Int = 16

            var marginEnd: Int = 16

            var cardViewCorner: Float = 24f

            var cardViewElevation: Float = 8f
        }
    }

    class IntentString {

        companion object {

            val USER_ID: String = "user_id"

            val USER_NAME: String = "user_name"

            val USER_AVATAR: String = "user_avatar"

            val USER_STATUS: String = "user_status"

            val LAST_ACTIVE: String = "last_user"

            val GROUP_ID: String = "group_id"

            val GROUP_NAME: String = "group_name"

            val GROUP_ICON: String = "group_icon"

            val GROUP_OWNER: String = "group_owner"

            val IMAGE_TYPE = "image/*"

            val AUDIO_TYPE = "audio/*"

            val DOCUMENT_TYPE = arrayOf("*/*")

            val EXTRA_MIME_TYPE = arrayOf("image/*", "video/*")

            val EXTRA_MIME_DOC = arrayOf("text/plane", "text/html", "application/pdf", "application/msword",
                "application/vnd.ms.excel", "application/mspowerpoint", "application/zip")

            val TITLE: String = "title"

            val POSITION: String = "position"

            val SESSION_ID: String = "session_id"

            val OUTGOING: String = "outgoing"

            val INCOMING: String = "incoming"

            val RECIVER_TYPE: String = "receiver_type"

            val URL: String = "image"

            val FILE_TYPE: String = "file_type"

            val ID: String = "id"

            val GROUP_DESCRIPTION:String="description"

            val USER_SCOPE: String="scope"

        }
    }
    }