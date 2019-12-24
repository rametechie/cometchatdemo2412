package com.orhan.comedchat_demo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.location.LocationManager
import android.media.AudioManager
import android.net.ConnectivityManager
import android.os.Build
import androidx.annotation.IdRes
import androidx.cardview.widget.CardView
import androidx.appcompat.widget.Toolbar
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.DisplayMetrics
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.cometchat.pro.models.Group
import com.cometchat.pro.models.User


class CommonUtil {

    companion object {

         var cm: ConnectivityManager?=null

        fun setCardView(cardView: androidx.cardview.widget.CardView) {
            cardView.cardElevation = StringContract.Dimensions.cardViewElevation
            cardView.radius = StringContract.Dimensions.cardViewCorner
            val param = cardView.layoutParams as RelativeLayout.LayoutParams
            param.marginEnd = StringContract.Dimensions.marginEnd
            param.marginStart = StringContract.Dimensions.marginStart
            cardView.layoutParams = param

        }


         fun setDrawable(color:Int,radius:Float): Drawable? {

            val drawable= GradientDrawable()
            drawable.shape= GradientDrawable.RECTANGLE
            drawable.cornerRadius= radius
            drawable.setColor(color)

            return drawable
        }



        fun checkPermission (context: Context):Boolean{

            return (context.getSystemService(Context.LOCATION_SERVICE) as LocationManager)
                    .isProviderEnabled(LocationManager.GPS_PROVIDER)
        }




        fun getAudioManager(context: Context): AudioManager {
            return context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        }



        fun isConnected(context: Context): Boolean {

            if (cm == null) {
                cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            }

            val var0 = cm!!.getActiveNetworkInfo()
            return null != var0 && var0!!.isConnectedOrConnecting()
        }





        fun dpToPx(context: Context, valueInDp: Float): Float {
            val resources = context.resources
            val metrics = resources.displayMetrics
            return valueInDp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
        }


        fun findById(parent: View, @IdRes resId: Int): Any {
            return parent.findViewById<View>(resId) as Any
        }

        fun setStatusBarColor(var0: Activity) {
            try {
                if (Build.VERSION.SDK_INT >= 21) {
                    val var2 = var0.window
                    var2.addFlags((-2147483648).toInt())
                    var2.statusBarColor = StringContract.Color.primaryDarkColor
                }
            } catch (var3: Exception) {
                var3.printStackTrace()
            }

        }

    }
}
